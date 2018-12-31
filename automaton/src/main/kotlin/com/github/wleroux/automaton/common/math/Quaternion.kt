package com.github.wleroux.automaton.common.math

import kotlin.math.acos

data class Quaternion(var w: Float, var x: Float, var y: Float, var z: Float) {
  companion object {
    operator fun invoke(axis: Vector3f, angle: Float): Quaternion {
      val s = Math.sin((angle / 2).toDouble()).toFloat()
      val w = Math.cos((angle / 2).toDouble()).toFloat()
      val x = axis.x * s
      val y = axis.y * s
      val z = axis.z * s
      return Quaternion(w, x, y, z).normalize()
    }

    fun fromToRotation(v1: Vector3f, v2: Vector3f): Quaternion {
      val cross = v1 cross v2
      val angle = acos((v1 dot v2) / (v1.magnitude * v2.magnitude))
      return invoke(if (cross.magnitude != 0f) cross else Vector3f(v1.y, v1.z, v1.x), angle)
    }
  }

  private val magnitude get() = Math.sqrt((w * w + x * x + y * y + z * z).toDouble()).toFloat()

  fun conjugate(): Quaternion {
    return Quaternion(w, -x, -y, -z)
  }

  operator fun times(o: Quaternion): Quaternion {
    return Quaternion(
            w * o.w - x * o.x - y * o.y - z * o.z,
            w * o.x + x * o.w - y * o.z + z * o.y,
            w * o.y + x * o.z + y * o.w - z * o.x,
            w * o.z - x * o.y + y * o.x + z * o.w
    )
  }

  operator fun times(s: Float): Quaternion {
    val halfAngle = Math.acos(w.toDouble()).toFloat()
    val sin = Math.sin(halfAngle.toDouble()).toFloat()
    val ax = x / sin
    val ay = y / sin
    val az = z / sin

    val newHalfAngle = halfAngle * s
    val newSin = Math.sin(newHalfAngle.toDouble()).toFloat()
    return Quaternion(
            Math.cos(newHalfAngle.toDouble()).toFloat(),
            ax * newSin,
            ay * newSin,
            az * newSin
    )
  }

  override fun equals(other: Any?): Boolean {
    return when (other) {
      is Quaternion -> Math.abs(w - other.w) <= 0.00001f &&
              Math.abs(x - other.x) <= 0.00001f &&
              Math.abs(y - other.y) <= 0.00001f &&
              Math.abs(z - other.z) <= 0.00001f
      else -> false
    }
  }

  private fun set(w: Float, x: Float, y: Float, z: Float): Quaternion {
    this.w = w
    this.x = x
    this.y = y
    this.z = z
    return this
  }

  fun normalize(): Quaternion {
    val m = magnitude
    set(w / m, x / m, y / m, z / m)
    return this
  }
}
