/******************************************************************************
 * Compilation:  javac CollisionSystem.java
 * Execution:    java CollisionSystem n               (n random particles)
 * java CollisionSystem < input.txt     (from a file)
 * Dependencies: StdDraw.kt Particle.kt MinPQ.kt
 * Data files:   https://algs4.cs.princeton.edu/61event/diffusion.txt
 * https://algs4.cs.princeton.edu/61event/diffusion2.txt
 * https://algs4.cs.princeton.edu/61event/diffusion3.txt
 * https://algs4.cs.princeton.edu/61event/brownian.txt
 * https://algs4.cs.princeton.edu/61event/brownian2.txt
 * https://algs4.cs.princeton.edu/61event/billiards5.txt
 * https://algs4.cs.princeton.edu/61event/pendulum.txt
 *
 * Creates n random particles and simulates their motion according
 * to the laws of elastic collisions.
 *
 */

package jingyiCheng.mscProject.algs

import java.awt.Color

/**
 * The `CollisionSystem` class represents a collection of particles
 * moving in the unit box, according to the laws of elastic collision.
 * This event-based simulation relies on a priority queue.
 *
 *
 * For additional documentation,
 * see [Section 6.1](https://algs4.cs.princeton.edu/61event) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class CollisionSystem
/**
 * Initializes a system with the specified collection of particles.
 * The individual particles will be mutated during the simulation.
 *
 * @param  particles the array of particles
 */
(particles: Array<Particle>) {
    private var pq: MinPQ<Event>? = null          // the priority queue
    private var t = 0.0          // simulation clock time
    private val particles: Array<Particle> = particles.clone()    // the array of particles

    // updates priority queue with all new events for particle a
    private fun predict(a: Particle?, limit: Double) {
        if (a == null) return

        // particle-particle collisions
        for (i in particles) {
            val dt = a.timeToHit(i)
            if (t + dt <= limit)
                pq!!.insert(Event(t + dt, a, i))
        }

        // particle-wall collisions
        val dtX = a.timeToHitVerticalWall()
        val dtY = a.timeToHitHorizontalWall()
        if (t + dtX <= limit) pq!!.insert(Event(t + dtX, a, null))
        if (t + dtY <= limit) pq!!.insert(Event(t + dtY, null, a))
    }

    // redraw all particles
    private fun redraw(limit: Double) {
        StdDraw.clear()
        for (i in particles)
            i.draw()
        StdDraw.show()
        StdDraw.pause(20)
        if (t < limit) {
            pq!!.insert(Event(t + 1.0 / HZ, null, null))
        }
    }

    /**
     * Simulates the system of particles for the specified amount of time.
     *
     * @param  limit the amount of time
     */
    fun simulate(limit: Double) {
        // initialize PQ with collision events and redraw event
        pq = MinPQ()
        for (i in particles)
            predict(i, limit)
        pq!!.insert(Event(0.0, null, null))        // redraw event

        // the main event-driven simulation loop
        while (!pq!!.isEmpty) {

            // get impending event, discard if invalidated
            val e = pq!!.delMin()
            if (!e.isValid) continue
            val a = e.a
            val b = e.b

            // physical collision, so update positions, and then simulation clock
            for (i in particles.indices)
                particles[i].move(e.time - t)
            t = e.time

            // process event
            if (a != null && b != null)
                a.bounceOff(b)              // particle-particle collision
            else if (a != null && b == null)
                a.bounceOffVerticalWall()   // particle-wall collision
            else if (a == null && b != null)
                b.bounceOffHorizontalWall() // particle-wall collision
            else if (a == null && b == null) redraw(limit)               // redraw event

            // update the priority queue with new collisions involving a or b
            predict(a, limit)
            predict(b, limit)
        }
    }

    /***************************************************************************
     * An event during a particle collision simulation. Each event contains
     * the time at which it will occur (assuming no supervening actions)
     * and the particles a and b involved.
     *
     * -  a and b both null:      redraw event
     * -  a null, b not null:     collision with vertical wall
     * -  a not null, b null:     collision with horizontal wall
     * -  a and b both not null:  binary collision between a and b
     *
     */
    internal class Event// create a new event to occur at time t involving a and b
    (val time: Double         // time that event is scheduled to occur
     , val a: Particle?, val b: Particle?       // particles involved in event, possibly null
    ) : Comparable<Event> {
        private val countA: Int = a?.count ?: -1
        private val countB: Int = b?.count ?: -1  // collision counts at event creation

        // has any collision occurred between when event was created and now?
        val isValid: Boolean
            get() {
                if (a != null && a.count != countA) return false
                return !(b != null && b.count != countB)
            }

        // compare times when two events will occur
        override fun compareTo(other: Event) = this.time.compareTo(other.time)
    }

    companion object {
        private const val HZ = 0.5    // number of redraw events per clock tick

        /**
         * Unit tests the `CollisionSystem` data type.
         * Reads in the particle collision system from a standard input
         * (or generates `N` random particles if a command-line integer
         * is specified); simulates the system.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            StdDraw.setCanvasSize(800, 800)

            // enable double buffering
            StdDraw.enableDoubleBuffering()

            // the array of particles
            val particles: Array<Particle>

            // create n random particles
            if (args.size == 1) {
                val n = Integer.parseInt(args[0])
                particles = Array(n) { Particle() }
            } else {
                val n = StdIn.readInt()
                particles = Array(n) {
                    val rx = StdIn.readDouble()
                    val ry = StdIn.readDouble()
                    val vx = StdIn.readDouble()
                    val vy = StdIn.readDouble()
                    val radius = StdIn.readDouble()
                    val mass = StdIn.readDouble()
                    val r = StdIn.readInt()
                    val g = StdIn.readInt()
                    val b = StdIn.readInt()
                    val color = Color(r, g, b)
                    Particle(rx, ry, vx, vy, radius, mass, color)
                }
            }// or read from standard input

            // create collision system and simulate
            val system = CollisionSystem(particles)
            system.simulate(10000.0)
        }
    }

}

/******************************************************************************
 * Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 * This file is part of algs4.jar, which accompanies the textbook
 *
 * Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 * Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 * http://algs4.cs.princeton.edu
 *
 *
 * algs4.jar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * algs4.jar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 */
