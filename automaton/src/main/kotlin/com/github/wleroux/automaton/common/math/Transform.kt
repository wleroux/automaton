package com.github.wleroux.automaton.common.math

data class Transform(
        var position: Vector3f = Vector3f(0f, 0f, 0f),
        var rotation: Quaternion = Quaternion(1f, 0f, 0f, 0f),
        var scale: Vector3f = Vector3f(1f, 1f, 1f)
)
