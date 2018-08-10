package jingyiCheng.mscProject.algs

import javax.sound.sampled.Clip

// for playing midi sound files on some older systems
import java.applet.Applet
import java.net.MalformedURLException

import java.io.File
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URL
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.SourceDataLine
import javax.sound.sampled.UnsupportedAudioFileException

/**
 * *Standard audio*. This class provides a basic capability for
 * creating, reading, and saving audio.
 *
 *
 * The audio format uses a sampling rate of 44,100 (CD quality audio), 16-bit, monaural.
 *
 *
 *
 * For additional documentation, see [Section 1.5](https://introcs.cs.princeton.edu/15inout) of
 * *Computer Science: An Interdisciplinary Approach* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object StdAudio {
    /**
     * The sample rate - 44,100 Hz for CD quality audio.
     */
    const val SAMPLE_RATE = 44100

    private const val BYTES_PER_SAMPLE = 2                // 16-bit audio
    private const val BITS_PER_SAMPLE = 16                // 16-bit audio
    private const val MAX_16_BIT = Short.MAX_VALUE.toDouble()     // 32,767
    private const val SAMPLE_BUFFER_SIZE = 4096

    private lateinit var line: SourceDataLine   // to play the sound
    private lateinit var buffer: ByteArray        // our internal buffer
    private var bufferSize = 0    // number of samples currently in internal buffer

    // static initializer
    init {
        try {
            // 44,100 samples per second, 16-bit audio, mono, signed PCM, little Endian
            val format = AudioFormat(SAMPLE_RATE.toFloat(), BITS_PER_SAMPLE, 1, true, false)
            val info = DataLine.Info(SourceDataLine::class.java, format)

            line = AudioSystem.getLine(info) as SourceDataLine
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE)

            // the internal buffer is a fraction of the actual buffer size, this choice is arbitrary
            // it gets divided because we can't expect the buffered data to line up exactly with when
            // the sound card decides to push out its samples.
            buffer = ByteArray(SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE / 3)
        } catch (e: LineUnavailableException) {
            println(e.message)
        }

        // no sound gets made before this call
        line.start()
    }

    /**
     * Closes standard audio.
     */
    fun close() {
        line.drain()
        line.stop()
    }

    /**
     * Writes one sample (between -1.0 and +1.0) to standard audio.
     * If the sample is outside the range, it will be clipped.
     *
     * @param  sample the sample to play
     * @throws IllegalArgumentException if the sample is `Double.NaN`
     */
    fun play(sample: Double) {
        var sample = sample

        // clip if outside [-1, +1]
        if (sample.isNaN()) throw IllegalArgumentException("sample is NaN")
        if (sample < -1.0) sample = -1.0
        if (sample > +1.0) sample = +1.0

        // convert to bytes
        val s = (MAX_16_BIT * sample).toShort()
        buffer[bufferSize++] = s.toByte()
        buffer[bufferSize++] = (s.toInt() shr 8).toShort().toByte()   // little Endian

        // send to sound card if buffer is full
        if (bufferSize >= buffer.size) {
            line.write(buffer, 0, buffer.size)
            bufferSize = 0
        }
    }

    /**
     * Writes the array of samples (between -1.0 and +1.0) to standard audio.
     * If a sample is outside the range, it will be clipped.
     *
     * @param  samples the array of samples to play
     * @throws IllegalArgumentException if any sample is `Double.NaN`
     * @throws IllegalArgumentException if `samples` is `null`
     */
    fun play(samples: DoubleArray?) {
        if (samples == null) throw IllegalArgumentException("argument to play() is null")
        for (i in samples)
            play(i)
    }

    /**
     * Reads audio samples from a file (in .wav or .au format) and returns
     * them as a double array with values between -1.0 and +1.0.
     *
     * @param  filename the name of the audio file
     * @return the array of samples
     */
    fun read(filename: String): DoubleArray {
        val data = readByte(filename)
        val n = data.size
        return DoubleArray(n / 2) {
            ((data[2 * it + 1].toInt() and 0xFF shl 8) + (data[2 * it].toInt() and 0xFF)).toShort() / MAX_16_BIT
        }
    }

    // return data as a byte array
    private fun readByte(filename: String): ByteArray {
        val data: ByteArray?
        val ais: AudioInputStream?
        try {
            // try to read from file
            val file = File(filename)
            if (file.exists()) {
                ais = AudioSystem.getAudioInputStream(file)
                val bytesToRead = ais!!.available()
                data = ByteArray(bytesToRead)
                val bytesRead = ais.read(data)
                if (bytesToRead != bytesRead)
                    throw IllegalStateException("read only $bytesRead of $bytesToRead bytes")
            } else {
                val url = StdAudio::class.java.getResource(filename)
                ais = AudioSystem.getAudioInputStream(url)
                val bytesToRead = ais!!.available()
                data = ByteArray(bytesToRead)
                val bytesRead = ais.read(data)
                if (bytesToRead != bytesRead)
                    throw IllegalStateException("read only $bytesRead of $bytesToRead bytes")
            }// try to read from URL
        } catch (e: IOException) {
            throw IllegalArgumentException("could not read '$filename'", e)
        } catch (e: UnsupportedAudioFileException) {
            throw IllegalArgumentException("unsupported audio format: '$filename'", e)
        }
        return data
    }

    /**
     * Saves the double array as an audio file (using .wav or .au format).
     *
     * @param  filename the name of the audio file
     * @param  samples the array of samples
     * @throws IllegalArgumentException if unable to save `filename`
     * @throws IllegalArgumentException if `samples` is `null`
     */
    fun save(filename: String, samples: DoubleArray?) {
        if (samples == null) throw IllegalArgumentException("samples[] is null")

        // assumes 44,100 samples per second
        // use 16-bit audio, mono, signed PCM, little Endian
        val format = AudioFormat(SAMPLE_RATE.toFloat(), 16, 1, true, false)
        val data = ByteArray(2 * samples.size)
        for (i in samples.indices) {
            val temp = (samples[i] * MAX_16_BIT).toShort().toInt()
            data[2 * i + 0] = temp.toByte()
            data[2 * i + 1] = (temp shr 8).toByte()
        }

        // now save the file
        try {
            val bais = ByteArrayInputStream(data)
            val ais = AudioInputStream(bais, format, samples.size.toLong())
            if (filename.endsWith(".wav") || filename.endsWith(".WAV"))
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, File(filename))
            else if (filename.endsWith(".au") || filename.endsWith(".AU"))
                AudioSystem.write(ais, AudioFileFormat.Type.AU, File(filename))
            else
                throw IllegalArgumentException("unsupported audio format: '$filename'")
        } catch (ioe: IOException) {
            throw IllegalArgumentException("unable to save file '$filename'", ioe)
        }
    }

    /**
     * Plays an audio file (in .wav, .mid, or .au format) in a background thread.
     *
     * @param filename the name of the audio file
     * @throws IllegalArgumentException if unable to play `filename`
     * @throws IllegalArgumentException if `filename` is `null`
     */
    @Synchronized
    fun play(filename: String?) {
        if (filename == null) throw IllegalArgumentException()

        val `is` = StdAudio::class.java.getResourceAsStream(filename)
                ?: throw IllegalArgumentException("could not read '$filename'")

        // code adapted from: http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
        try {
            AudioSystem.getAudioInputStream(`is`)
            Thread(Runnable { stream(filename) }).start()
        } catch (e: UnsupportedAudioFileException) {
            playApplet(filename)
            return
        } catch (ioe: IOException) {
            throw IllegalArgumentException("could not play '$filename'", ioe)
        }
    }


    // play sound file using Applet.newAudioClip();
    private fun playApplet(filename: String) {
        var url: URL? = null
        try {
            val file = File(filename)
            if (file.canRead()) url = file.toURI().toURL()
        } catch (e: MalformedURLException) {
            throw IllegalArgumentException("could not play '$filename'", e)
        }
        if (url == null) throw IllegalArgumentException("could not play '$filename'")
        val clip = Applet.newAudioClip(url)
        clip.play()
    }

    // https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
    // play a wav or aif file
    // javax.sound.sampled.Clip fails for long clips (on some systems)
    private fun stream(filename: String?) {
        var line: SourceDataLine? = null
        val BUFFER_SIZE = 4096 // 4K buffer

        try {
            val `is` = StdAudio::class.java.getResourceAsStream(filename)
            val ais = AudioSystem.getAudioInputStream(`is`)
            val audioFormat = ais.format
            val info = DataLine.Info(SourceDataLine::class.java, audioFormat)
            line = AudioSystem.getLine(info) as SourceDataLine
            line.open(audioFormat)
            line.start()
            val samples = ByteArray(BUFFER_SIZE)
            var count = 0
            while (run { count = ais.read(samples, 0, BUFFER_SIZE); count != -1 }) {
                line.write(samples, 0, count)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: UnsupportedAudioFileException) {
            e.printStackTrace()
        } catch (e: LineUnavailableException) {
            e.printStackTrace()
        } finally {
            line?.let {
                line.drain()
                line.close()
            }
        }
    }

    /**
     * Loops an audio file (in .wav, .mid, or .au format) in a background thread.
     *
     * @param filename the name of the audio file
     * @throws IllegalArgumentException if `filename` is `null`
     */
    @Synchronized
    fun loop(filename: String?) {
        if (filename == null) throw IllegalArgumentException()

        // code adapted from: http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
        try {
            val clip = AudioSystem.getClip()
            val `is` = StdAudio::class.java.getResourceAsStream(filename)
            val ais = AudioSystem.getAudioInputStream(`is`)
            clip.open(ais)
            clip.loop(Clip.LOOP_CONTINUOUSLY)
        } catch (e: UnsupportedAudioFileException) {
            throw IllegalArgumentException("unsupported audio format: '$filename'", e)
        } catch (e: LineUnavailableException) {
            throw IllegalArgumentException("could not play '$filename'", e)
        } catch (e: IOException) {
            throw IllegalArgumentException("could not play '$filename'", e)
        }

    }

    // create a note (sine wave) of the given frequency (Hz), for the given
    // duration (seconds) scaled to the given volume (amplitude)
    private fun note(hz: Double, duration: Double, amplitude: Double): DoubleArray {
        val n = (SAMPLE_RATE * duration).toInt()
        return DoubleArray(n + 1) { amplitude * Math.sin(2.0 * Math.PI * it * hz / SAMPLE_RATE) }
    }

    /**
     * Test client - play an A major scale to standard audio.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        // 440 Hz for 1 sec
        val freq = 440.0
        for (i in 0..SAMPLE_RATE) {
            play(0.5 * Math.sin(2.0 * Math.PI * freq * i / SAMPLE_RATE))
        }

        // scale increments
        val steps = intArrayOf(0, 2, 4, 5, 7, 9, 11, 12)
        steps.map { 440.0 * Math.pow(2.0, it / 12.0) }
                .forEach { play(note(it, 1.0, 0.5)) }

        // need to call this in non-interactive stuff so the program doesn't terminate
        // until all the sound leaves the speaker.
        close()
    }
}// can not instantiate

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/