package com.github.wleroux.automaton.program

import com.github.wleroux.automaton.platform.BYTES
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30

class Mesh(vertices: Array<Float>, indices: Array<Int>, private val attributes: List<Attribute>) {
    data class Attribute(val key: String, val type: Int, val size: Int) {
        val bytes: Int
            get() = when (type) {
                GL_FLOAT -> Float.BYTES
                else -> throw IllegalArgumentException("Unknown type: $type")
            }
    }

    private val id = GL30.glGenVertexArrays()
    private val size = indices.size

    init {
        GL30.glBindVertexArray(id)

        val vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        val vertexBuffer = BufferUtils.createFloatBuffer(vertices.size * 8)
        vertices.forEach { vertex -> vertexBuffer.put(vertex) }
        vertexBuffer.flip()

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)
        val stride = attributes.sumBy { it.size * it.bytes }
        var offset = 0L
        attributes.forEachIndexed { index, attribute ->
            glVertexAttribPointer(index, attribute.size, attribute.type, false, stride, offset)
            glEnableVertexAttribArray(index)
            offset += attribute.size * attribute.bytes
        }

        val ibo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
        val indicesBuffer = BufferUtils.createIntBuffer(indices.size)
        indices.forEach { index -> indicesBuffer.put(index) }
        indicesBuffer.flip()
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

        GL30.glBindVertexArray(0)
    }

    fun bind(block: Mesh.() -> Unit) {
        GL30.glBindVertexArray(id)
        attributes.forEachIndexed { index, _ ->
            glEnableVertexAttribArray(index)
        }

        block.invoke(this)

        attributes.forEachIndexed { index, _ ->
            glDisableVertexAttribArray(index)
        }

        GL30.glBindVertexArray(0)
    }

    fun draw() {
        glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0)
    }
}