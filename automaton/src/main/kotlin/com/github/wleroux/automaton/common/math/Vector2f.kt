package com.github.wleroux.automaton.common.math

import java.lang.Math.sqrt
import java.nio.FloatBuffer

data class Vector2f(var x: Float, var y: Float) {
  val magnitude: Float
    get() = sqrt((x * x + y * y).toDouble()).toFloat()

  companion object {
    val AXIS_X = Vector2f(1f, 0f)
    val AXIS_NEG_X = Vector2f(-1f, 0f)
    val AXIS_Y = Vector2f(0f, 1f)
    val AXIS_NEG_Y = Vector2f(0f, -1f)
    val ZERO = Vector2f(0f, 0f)
  }

  override fun equals(other: Any?): Boolean {
    return when (other) {
      is Vector2f -> Math.abs(x - other.x) <= 0.00001f &&
              Math.abs(y - other.y) <= 0.00001f
      else -> false
    }
  }

  operator fun plus(o: Vector2f) = Vector2f(this.x + o.x, this.y + o.y)
  operator fun minus(o: Vector2f) = Vector2f(this.x - o.x, this.y - o.y)
  operator fun times(mat4: Matrix4f): Vector2f {
    return Vector2f(
            x * mat4.m00 + y * mat4.m01,
            x * mat4.m10 + y * mat4.m11
    )
  }
  operator fun times(s: Float) = Vector2f(this.x * s, this.y * s)
  operator fun div(s: Float) = Vector2f(this.x / s, this.y / s)
  fun normalize() = times(1f / magnitude)
  infix fun dot(o: Vector2f) = (x * o.x + y * o.y)

  fun store(buffer: FloatBuffer) {
    buffer.put(x).put(y)
  }
}