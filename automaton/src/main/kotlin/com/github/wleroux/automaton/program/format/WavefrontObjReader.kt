package com.github.wleroux.automaton.program.format

import com.github.wleroux.automaton.math.Vector2f
import com.github.wleroux.automaton.math.Vector3f
import com.github.wleroux.automaton.program.Mesh
import com.github.wleroux.automaton.program.Vertex
import org.lwjgl.opengl.GL11.GL_FLOAT

object WavefrontObjReader {

    private const val FLOAT_REGEX = "[-+]?\\d+(?:\\.\\d+)?"
    private const val INT_REGEX = "[-+]?\\d+"
    private val POSITION_PATTERN = Regex("^v ($FLOAT_REGEX) ($FLOAT_REGEX) ($FLOAT_REGEX)$")
    private val TEXTURE_COORDINATE_PATTERN = Regex("^vt ($FLOAT_REGEX) ($FLOAT_REGEX)$")
    private val NORMAL_PATTERN = Regex("^vn ($FLOAT_REGEX) ($FLOAT_REGEX) ($FLOAT_REGEX)$")
    private val VERTEX_PATTERN = Regex("^f ($INT_REGEX)/($INT_REGEX)/($INT_REGEX) ($INT_REGEX)/($INT_REGEX)/($INT_REGEX) ($INT_REGEX)/($INT_REGEX)/($INT_REGEX)$")

    fun read(source: String): Mesh {
        val positions = mutableListOf<Vector3f>()
        val textureCoordinates = mutableListOf<Vector2f>()
        val normals = mutableListOf<Vector3f>()

        val vertices = mutableListOf<Float>()
        val indices = mutableListOf<Int>()
        var vertexCount = 0
        source.reader().forEachLine {line ->
            when {
                POSITION_PATTERN.matches(line) -> positions += parsePosition(line)
                TEXTURE_COORDINATE_PATTERN.matches(line) -> textureCoordinates += parseTextureCoordinate(line)
                NORMAL_PATTERN.matches(line) -> normals += parseNormal(line)
                VERTEX_PATTERN.matches(line) -> {
                    val vectors = parseVertices(line, positions, textureCoordinates, normals)
                    vectors.forEach { vector ->
                        vertices += listOf(
                                vector.position.x, vector.position.y, vector.position.z,
                                vector.textureCoordinate.x, vector.textureCoordinate.y,
                                vector.normal.x, vector.normal.y, vector.normal.z
                        )
                    }

                    indices += listOf(vertexCount + 0, vertexCount + 1, vertexCount + 2)
                    vertexCount += 3
                }
                else -> Unit
            }
        }

        return Mesh(vertices.toTypedArray(), indices.toTypedArray(), listOf(
                Mesh.Attribute("position", GL_FLOAT, 3),
                Mesh.Attribute("texCoord", GL_FLOAT, 2),
                Mesh.Attribute("normal", GL_FLOAT, 3)
        ))
    }

    private fun parsePosition(line: String): Vector3f {
        val values = POSITION_PATTERN.find(line)!!.groups
        return Vector3f(
                values[1]!!.value.toFloat(),
                values[2]!!.value.toFloat(),
                values[3]!!.value.toFloat()
        )
    }

    private fun parseTextureCoordinate(line: String): Vector2f {
        val values = TEXTURE_COORDINATE_PATTERN.find(line)!!.groups
        return Vector2f(
                values[1]!!.value.toFloat(),
                values[2]!!.value.toFloat()
        )
    }

    private fun parseNormal(line: String): Vector3f {
        val values = NORMAL_PATTERN.find(line)!!.groups
        return Vector3f(
                values[1]!!.value.toFloat(),
                values[2]!!.value.toFloat(),
                values[3]!!.value.toFloat()
        )
    }

    private fun parseVertices(line: String, positions: List<Vector3f>, textureCoordinates: List<Vector2f>, normals: List<Vector3f>): List<Vertex> {
        val values = VERTEX_PATTERN.find(line)!!.groups
        return listOf(Vertex(
                positions[values[1]!!.value.toInt() - 1],
                textureCoordinates[values[2]!!.value.toInt() - 1],
                normals[values[3]!!.value.toInt() - 1]
        ), Vertex(
                positions[values[4]!!.value.toInt() - 1],
                textureCoordinates[values[5]!!.value.toInt() - 1],
                normals[values[6]!!.value.toInt() - 1]
        ), Vertex(
                positions[values[7]!!.value.toInt() - 1],
                textureCoordinates[values[8]!!.value.toInt() - 1],
                normals[values[9]!!.value.toInt() - 1]
        ))
    }
}