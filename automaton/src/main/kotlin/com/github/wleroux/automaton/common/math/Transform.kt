package com.github.wleroux.automaton.common.math

data class Transform(
        val position: Vector3f = Vector3f(0f, 0f, 0f),
        val rotation: Quaternion = Quaternion(1f, 0f, 0f, 0f),
        val scale: Vector3f = Vector3f(1f, 1f, 1f)
)
