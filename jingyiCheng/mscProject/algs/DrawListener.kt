/******************************************************************************
 * Compilation:  javac DrawListener.java
 * Execution:    none
 * Dependencies: none
 *
 * Interface that accompanies Draw.kt.
 ******************************************************************************/

package jingyiCheng.mscProject.algs

/**
 * @author Jingyi Cheng
 */

interface DrawListener {

    /**
     * Invoked when the mouse has been pressed.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    fun mousePressed(x: Double, y: Double)

    /**
     * Invoked when the mouse has been dragged.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    fun mouseDragged(x: Double, y: Double)

    /**
     * Invoked when the mouse has been released.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    fun mouseReleased(x: Double, y: Double)

    /**
     * Invoked when a key has been typed.
     *
     * @param c the character typed
     */
    fun keyTyped(c: Char)

    /**
     * Invoked when a key has been pressed.
     *
     * @param keycode the key combination pressed
     */
    fun keyPressed(keycode: Int)

    /**
     * Invoked when a key has been released.
     *
     * @param keycode the key combination released
     */
    fun keyReleased(keycode: Int)
}

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
