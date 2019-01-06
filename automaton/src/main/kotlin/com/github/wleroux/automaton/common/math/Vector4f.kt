package com.github.wleroux.automaton.common.math

import java.lang.Math.*
import java.nio.FloatBuffer

data class Vector4f(var x: Float, var y: Float, var z: Float, var w: Float) {
  val magnitude: Float
    get() = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

  companion object {
    val AXIS_X = Vector4f(1f, 0f, 0f, 0f)
    val AXIS_NEG_X = Vector4f(-1f, 0f, 0f, 0f)
    val AXIS_Y = Vector4f(0f, 1f, 0f, 0f)
    val AXIS_NEG_Y = Vector4f(0f, -1f, 0f, 0f)
    val AXIS_Z = Vector4f(0f, 0f, 1f, 0f)
    val AXIS_NEG_Z = Vector4f(0f, 0f, -1f, 0f)
    val AXIS_W = Vector4f(0f, 0f, 0f, 1f)
    val AXIS_NEG_W = Vector4f(0f, 0f, 0f, -1f)
    val ZERO = Vector4f(0f, 0f, 0f, 0f)
  }

  override fun equals(other: Any?): Boolean {
    return when (other) {
      is Vector4f -> abs(x - other.x) <= 0.00001f &&
              abs(y - other.y) <= 0.00001f &&
              abs(z - other.z) <= 0.00001f &&
              abs(w - other.w) <= 0.00001f
      else -> false
    }
  }

  operator fun plus(o: Vector4f) = Vector4f(this.x + o.x, this.y + o.y, this.z + o.z, this.w + o.w)
  operator fun minus(o: Vector4f) = Vector4f(this.x - o.x, this.y - o.y, this.z - o.z, this.w - o.w)
  operator fun times(mat4: Matrix4f): Vector4f {
    return Vector4f(
            x * mat4.m00 + y * mat4.m01 + z * mat4.m02 + w * mat4.m03,
            x * mat4.m10 + y * mat4.m11 + z * mat4.m12 + w * mat4.m13,
            x * mat4.m20 + y * mat4.m21 + z * mat4.m22 + w * mat4.m23,
            x * mat4.m30 + y * mat4.m31 + z * mat4.m32 + w * mat4.m33
    )
  }
  operator fun times(s: Float) = Vector4f(this.x * s, this.y * s, this.z * s, this.w * s)
  operator fun div(s: Float) = Vector4f(this.x / s, this.y / s, this.z / s, this.w / s)
  fun normalize() = times(1f / magnitude)

  fun store(buffer: FloatBuffer) {
    buffer.put(x).put(y).put(z).put(w)
  }
}