package com.github.wleroux.automaton.data

import com.github.wleroux.automaton.common.math.Quaternion
import com.github.wleroux.automaton.common.math.Transform
import com.github.wleroux.automaton.common.math.Vector2f
import com.github.wleroux.automaton.common.math.Vector3f

data class Camera(
        val center: Vector2f,
        var zoomLevel: Float,
        var rotationAngle: Float
) {
    private val viewOffset get() = Vector3f(0f, 0f, -1f).times(rotation).times(zoomLevel)
    private val viewAngle: Quaternion = Quaternion(Vector3f.AXIS_X, Math.PI.toFloat() * (4f / 16f))
    val position get() = Vector3f(center.x, 0f, center.y) + viewOffset
    val rotation get() = viewAngle * Quaternion(Vector3f.AXIS_Y, rotationAngle)

    fun toTransform(): Transform = Transform(
            position = position,
            rotation = rotation
    )
}
