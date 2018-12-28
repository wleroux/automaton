package com.github.wleroux.automaton.program

import com.github.wleroux.automaton.math.Vector2f
import com.github.wleroux.automaton.math.Vector3f
import java.nio.FloatBuffer

data class Vertex(
        val position: Vector3f,
        val textureCoordinate: Vector2f,
        val normal: Vector3f
) {
    fun store(buffer: FloatBuffer) {
        position.store(buffer)
        textureCoordinate.store(buffer)
        normal.store(buffer)
    }
}