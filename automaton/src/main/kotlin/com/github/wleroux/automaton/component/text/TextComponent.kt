package com.github.wleroux.automaton.component.text

import com.github.wleroux.automaton.component.text.font.Font
import com.github.wleroux.automaton.component.text.font.FontMeshCreator
import com.github.wleroux.automaton.loadText
import com.github.wleroux.automaton.loadTexture
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.automaton.math.Matrix4f
import com.github.wleroux.automaton.math.Vector2f
import com.github.wleroux.automaton.program.Mesh
import com.github.wleroux.automaton.program.Program
import com.github.wleroux.automaton.program.Texture
import com.github.wleroux.keact.api.Component
import org.lwjgl.opengl.GL11.*

class TextComponent: Component<Unit, TextComponent.TextProperties>(Unit) {
    data class TextProperties(
            val text: String,
            val size: Int,
            val color: Color,
            val font: Font
    )

    lateinit var program: Program
    override fun componentWillMount() {
        program = Program.build {
            vertexShader(loadText("com/github/wleroux/automaton/program/sdf/sdf.vs.glsl"))
            uniform("projection")

            fragmentShader(loadText("com/github/wleroux/automaton/program/sdf/sdf.fs.glsl"))
            uniform("BaseColor")
            uniform("Color", "Width", "Edge")
            uniform("BorderColor", "BorderWidth", "BorderEdge", "BorderOffset")
        }
    }

    lateinit var mesh: Mesh
    lateinit var texture: Texture
    var textWidth: Float = 0f
    var textHeight: Float = 0f
    override fun componentDidUpdate(previousProperties: TextProperties, previousState: Unit) {
        mesh = FontMeshCreator.generateTextMesh(properties.text, properties.font, properties.size)
        textWidth = FontMeshCreator.getWidth(properties.text, properties.font, properties.size)
        textHeight = FontMeshCreator.getHeight(properties.text, properties.font, properties.size)
        texture = loadTexture(checkNotNull(properties.font.pages[0]))
    }

    override fun preferredWidth(parentWidth: Int, parentHeight: Int): Int = textWidth.toInt()
    override fun preferredHeight(parentWidth: Int, parentHeight: Int): Int = textHeight.toInt()
    override fun render() {
        program.use {
            glViewport(x, y, width, height)
            glClear(GL_DEPTH_BUFFER_BIT)
            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

            setUniform("projection", Matrix4f.orthogonal(
                    0f,
                    width.toFloat(),
                    height.toFloat(),
                    0f,
                    -1f,
                    1f
            ))
            setUniform("BaseColor", texture)

            setUniform("Color", properties.color)
            setUniform("Width", 0.4f)
            setUniform("Edge", 0.01f)

            setUniform("BorderColor", Color.BLACK)
            setUniform("BorderWidth", 0.7f)
            setUniform("BorderEdge", 0.1f)
            setUniform("BorderOffset", Vector2f(0f, 0f))
            mesh.bind {
                mesh.draw()
            }
        }
    }
}