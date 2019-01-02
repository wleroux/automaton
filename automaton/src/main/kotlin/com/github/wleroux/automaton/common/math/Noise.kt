package com.github.wleroux.automaton.common.math

import org.lwjgl.stb.STBPerlin.stb_perlin_fbm_noise3
import org.lwjgl.stb.STBPerlin.stb_perlin_noise3
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Computes a random value at the coordinate (x,y,z).
 *
 * <p>Adjacent random values are continuous but the noise fluctuates its randomness with period 1, i.e. takes on wholly unrelated values at integer points.
 * Specifically, this implements Ken Perlin's revised noise function from 2002.</p>
 *
 * <p>The "wrap" parameters can be used to create wraparound noise that wraps at powers of two. The numbers MUST be powers of two. Specify 0 to mean "don't
 * care". (The noise always wraps every 256 due details of the implementation, even if you ask for larger or no wrapping.)</p>
 *
 * @param x      the x coordinate
 * @param y      the y coordinate
 * @param z      the z coordinate
 * @param x_wrap 0, or a power of two value that specifies the x wraparound coordinate
 * @param y_wrap 0, or a power of two value that specifies the y wraparound coordinate
 * @param z_wrap 0, or a power of two value that specifies the z wraparound coordinate
 */
fun perlin_noise(seed: Long, x: Float, y: Float, z: Float, lacunarity: Float = 1f, gain: Float = 1f, octaves: Int = 1, x_wrap: Int = 256, y_wrap: Int = 256, z_wrap: Int = 256): Float {
    val xSeed = Random(seed).nextFloat() * x_wrap
    val ySeed = Random(seed).nextFloat() * y_wrap
    val zSeed = Random(seed).nextFloat() * z_wrap

    val value = stb_perlin_fbm_noise3(
            xSeed + x, ySeed + y, zSeed + z,
            lacunarity,
            gain,
            octaves,
            x_wrap, y_wrap, z_wrap)
    // normalize random value to [0,1[
    val d = sqrt(3f) / 2
    return (value + d) / (2f * d)
}