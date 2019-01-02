package com.github.wleroux.automaton.common.math

import java.lang.Math.sqrt

data class Vector2i(var x: Int, var y: Int) {
  val magnitude: Float
    get() = sqrt((x * x + y * y).toDouble()).toFloat()

  companion object {
    val AXIS_X = Vector2i(1, 0)
    val AXIS_NEG_X = Vector2i(-1, 0)
    val AXIS_Y = Vector2i(0, 1)
    val AXIS_NEG_Y = Vector2i(0, -1)
    val ZERO = Vector2i(0, 0)
  }

  operator fun plus(o: Vector2i) = Vector2i(this.x + o.x, this.y + o.y)
  operator fun minus(o: Vector2i) = Vector2i(this.x - o.x, this.y - o.y)
  operator fun times(s: Int) = Vector2i(this.x * s, this.y * s)
  operator fun div(s: Int) = Vector2i(this.x / s, this.y / s)
  infix fun dot(o: Vector2i) = (x * o.x + y * o.y)

  fun toVector2f() = Vector2f(x.toFloat(), y.toFloat())

  override fun hashCode(): Int {
    var result = x
    result = 31 * result + y
    return result
  }
  override fun equals(other: Any?): Boolean {
    return when (other) {
      is Vector2i -> x == other.x && y == other.y
      else -> false
    }
  }
}
