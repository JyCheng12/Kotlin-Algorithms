/******************************************************************************
 * Compilation:  javac Particle.java
 * Execution:    none
 * Dependencies: StdDraw.kt
 *
 * A particle moving in the unit box with a given position, velocity,
 * radius, and mass.
 *
 */

package jingyiCheng.mscProject.algs

import java.awt.Color

/**
 * The `Particle` class represents a particle moving in the unit box,
 * with a given position, velocity, radius, and mass. Methods are provided
 * for moving the particle and for predicting and resolvling elastic
 * collisions with vertical walls, horizontal walls, and other particles.
 * This data type is mutable because the position and velocity change.
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
class Particle {
    private var rx = 0.0
    private var ry = 0.0        // position
    private var vx = 0.0
    private var vy = 0.0        // velocity
    var count = 0            // number of collisions so far
        private set
    private val radius: Double  // radius
    private val mass: Double    // mass
    private val color: Color    // color

    /**
     * Initializes a particle with the specified position, velocity, radius, mass, and color.
     *
     * @param  rx *x*-coordinate of position
     * @param  ry *y*-coordinate of position
     * @param  vx *x*-coordinate of velocity
     * @param  vy *y*-coordinate of velocity
     * @param  radius the radius
     * @param  mass the mass
     * @param  color the color
     */
    constructor(rx: Double, ry: Double, vx: Double, vy: Double, radius: Double, mass: Double, color: Color) {
        this.vx = vx
        this.vy = vy
        this.rx = rx
        this.ry = ry
        this.radius = radius
        this.mass = mass
        this.color = color
    }

    /**
     * Initializes a particle with a random position and velocity.
     * The position is uniform in the unit box; the velocity in
     * either direciton is chosen uniformly at random.
     */
    constructor() {
        rx = StdRandom.uniform(0.0, 1.0)
        ry = StdRandom.uniform(0.0, 1.0)
        vx = StdRandom.uniform(-0.005, 0.005)
        vy = StdRandom.uniform(-0.005, 0.005)
        radius = 0.01
        mass = 0.5
        color = Color.BLACK
    }

    /**
     * Moves this particle in a straight line (based on its velocity)
     * for the specified amount of time.
     *
     * @param  dt the amount of time
     */
    fun move(dt: Double) {
        rx += vx * dt
        ry += vy * dt
    }

    /**
     * Draws this particle to standard draw.
     */
    fun draw() {
        StdDraw.penColor = color
        StdDraw.filledCircle(rx, ry, radius)
    }

    /**
     * Returns the amount of time for this particle to collide with the specified
     * particle, assuming no interening collisions.
     *
     * @param  that the other particle
     * @return the amount of time for this particle to collide with the specified
     * particle, assuming no interening collisions;
     * `Double.POSITIVE_INFINITY` if the particles will not collide
     */
    fun timeToHit(that: Particle): Double {
        if (this === that) return INFINITY
        val dx = that.rx - this.rx
        val dy = that.ry - this.ry
        val dvx = that.vx - this.vx
        val dvy = that.vy - this.vy
        val dvdr = dx * dvx + dy * dvy
        if (dvdr > 0) return INFINITY
        val dvdv = dvx * dvx + dvy * dvy
        val drdr = dx * dx + dy * dy
        val sigma = this.radius + that.radius
        val d = dvdr * dvdr - dvdv * (drdr - sigma * sigma)
        return if (d < 0) INFINITY else -(dvdr + Math.sqrt(d)) / dvdv
    }

    /**
     * Returns the amount of time for this particle to collide with a vertical
     * wall, assuming no interening collisions.
     *
     * @return the amount of time for this particle to collide with a vertical wall,
     * assuming no interening collisions;
     * `Double.POSITIVE_INFINITY` if the particle will not collide
     * with a vertical wall
     */
    fun timeToHitVerticalWall() = when {
        vx > 0 -> (1.0 - rx - radius) / vx
        vx < 0 -> (radius - rx) / vx
        else -> INFINITY
    }

    /**
     * Returns the amount of time for this particle to collide with a horizontal
     * wall, assuming no interening collisions.
     *
     * @return the amount of time for this particle to collide with a horizontal wall,
     * assuming no interening collisions;
     * `Double.POSITIVE_INFINITY` if the particle will not collide
     * with a horizontal wall
     */
    fun timeToHitHorizontalWall() = when {
        vy > 0 -> (1.0 - ry - radius) / vy
        vy < 0 -> (radius - ry) / vy
        else -> INFINITY
    }

    /**
     * Updates the velocities of this particle and the specified particle according
     * to the laws of elastic collision. Assumes that the particles are colliding
     * at this instant.
     *
     * @param  that the other particle
     */
    fun bounceOff(that: Particle) {
        val dx = that.rx - this.rx
        val dy = that.ry - this.ry
        val dvx = that.vx - this.vx
        val dvy = that.vy - this.vy
        val dvdr = dx * dvx + dy * dvy             // dv dot dr
        val dist = this.radius + that.radius   // distance between particle centers at collison

        // magnitude of normal force
        val magnitude = 2.0 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist)

        // normal force, and in x and y directions
        val fx = magnitude * dx / dist
        val fy = magnitude * dy / dist

        // update velocities according to normal force
        this.vx += fx / this.mass
        this.vy += fy / this.mass
        that.vx -= fx / that.mass
        that.vy -= fy / that.mass

        // update collision counts
        this.count++
        that.count++
    }

    /**
     * Updates the velocity of this particle upon collision with a vertical
     * wall (by reflecting the velocity in the *x*-direction).
     * Assumes that the particle is colliding with a vertical wall at this instant.
     */
    fun bounceOffVerticalWall() {
        vx = -vx
        count++
    }

    /**
     * Updates the velocity of this particle upon collision with a horizontal
     * wall (by reflecting the velocity in the *y*-direction).
     * Assumes that the particle is colliding with a horizontal wall at this instant.
     */
    fun bounceOffHorizontalWall() {
        vy = -vy
        count++
    }

    /**
     * Returns the kinetic energy of this particle.
     * The kinetic energy is given by the formula 1/2 *m* *v*<sup>2</sup>,
     * where *m* is the mass of this particle and *v* is its velocity.
     *
     * @return the kinetic energy of this particle
     */
    fun kineticEnergy() = 0.5 * mass * (vx * vx + vy * vy)

    companion object {
        private val INFINITY = Double.POSITIVE_INFINITY
    }
}

/******************************************************************************
 * This Kotlin file is automatically translated from Java using the
 * Java-to-Kotlin converter by JetBrains with manual adjustments.
 *
 * Following is the copyright contents of the original file:
 *
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This original file is part of algs4.jar, which accompanies the
 *  textbook
 *  Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *  Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *  http://algs4.cs.princeton.edu
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
 */