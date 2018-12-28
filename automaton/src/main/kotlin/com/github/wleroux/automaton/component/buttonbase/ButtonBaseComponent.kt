package com.github.wleroux.automaton.component.buttonbase

import com.github.wleroux.automaton.loadText
import com.github.wleroux.automaton.loadTexture
import com.github.wleroux.automaton.math.Color4f
import com.github.wleroux.automaton.math.Matrix4f
import com.github.wleroux.automaton.math.Vector2f
import com.github.wleroux.automaton.program.Mesh
import com.github.wleroux.automaton.program.Program
import com.github.wleroux.automaton.program.Texture
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL11.glViewport

class ButtonBaseComponent: Component<Unit, ButtonBaseComponent.ButtonBaseProperties>() {
    data class ButtonBaseProperties(
            val color: Color4f,
            val nodes: List<Node<*, *>>
    )
    data class ButtonBaseState(
            val width: Int,
            val height: Int
    )

    override lateinit var properties: ButtonBaseProperties
    override var state: Unit = Unit

    override fun asNodes() = listOf(
            padding {
                top = 8
                right = 16
                bottom = 8
                left = 16

                properties.nodes.forEach { +it }
            }
    )

    lateinit var program: Program
    lateinit var texture: Texture
    lateinit var mesh: Mesh
    override fun componentDidMount() {
        program = Program.build {
            vertexShader(loadText("com/github/wleroux/automaton/program/sdf/sdf.vs.glsl"))
            uniform("projection")

            fragmentShader(loadText("com/github/wleroux/automaton/program/sdf/sdf.fs.glsl"))
            uniform("BaseColor")
            uniform("Color", "Width", "Edge")
            uniform("BorderColor", "BorderWidth", "BorderEdge", "BorderOffset")
        }
        texture = loadTexture("automaton/asset/ButtonBase.png")
    }

    var lastWidth = 0
    var lastHeight = 0
    override fun render() {
        if (lastWidth != width && lastHeight != height) {
            mesh = generateMesh()
        }

        // Render button base
        program.use {
            glViewport(x, y, width, height)
            setUniform("projection", Matrix4f.orthogonal(
                    0f,
                    width.toFloat(),
                    height.toFloat(),
                    0f,
                    -1f, 1f
            ))

            setUniform("Color", properties.color)
            setUniform("Width", 0.5f)
            setUniform("Edge", 0.2f)

            setUniform("BorderColor", Color4f(0.0f, 0.0f, 0.0f, 1.0f))
            setUniform("BorderWidth", 0.7f)
            setUniform("BorderEdge", 0.1f)
            setUniform("BorderOffset", Vector2f(0f, 0f))

            setUniform("BaseColor", texture)
            mesh.bind {
                mesh.draw()
            }
        }

        // Render sub
        super.render()
    }

    private fun generateMesh(): Mesh {
        val vertices = mutableListOf<Float>()
        val indices = mutableListOf<Int>()
        var indexCount = 0
        (0..2).forEach { xQuad ->
            (0..2).forEach { yQuad ->
                val xPos = when (xQuad) {
                    0 -> 0f
                    1 -> 16f
                    else -> width - 16f
                }
                val xWidth = when (xQuad) {
                    0 -> 16f
                    1 -> width - 32f
                    else -> 16f
                }
                val yPos = when (yQuad) {
                    0 -> 0f
                    1 -> 16f
                    else -> height - 16f
                }
                val yHeight = when (yQuad) {
                    0 -> 16f
                    1 -> height - 32f
                    else -> 16f
                }

                vertices += listOf(
                        xPos, yPos,
                        (xQuad / 3f), (yQuad / 3f),
                        xPos + xWidth, yPos,
                        ((xQuad + 1f) / 3f), (yQuad / 3f),
                        xPos + xWidth, yPos + yHeight,
                        ((xQuad + 1f) / 3f), ((yQuad + 1f) / 3f),
                        xPos, yPos + yHeight,
                        (xQuad / 3f), ((yQuad + 1f) / 3f)
                )
                indices += listOf(
                        indexCount + 0, indexCount + 1, indexCount + 2,
                        indexCount + 2, indexCount + 3, indexCount + 0
                )
                indexCount += 4
            }
        }

        return Mesh(
                vertices.toTypedArray(),
                indices.toTypedArray(),
                listOf(
                        Mesh.Attribute("position", GL_FLOAT, 2),
                        Mesh.Attribute("texCoord", GL_FLOAT, 2)
                )
        )
    }
}