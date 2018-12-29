package com.github.wleroux.automaton.component.text

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
            val theme: TextTheme
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
    var textWidth: Int = 0
    var textHeight: Int = 0
    override fun componentDidUpdate(previousProperties: TextProperties, previousState: Unit) {
        mesh = FontMeshCreator.generateTextMesh(properties.text, properties.theme.font, properties.theme.size)
        textWidth = FontMeshCreator.getWidth(properties.text, properties.theme.font, properties.theme.size).toInt()
        textHeight = FontMeshCreator.getHeight(properties.text, properties.theme.font, properties.theme.size).toInt()
        texture = loadTexture(checkNotNull(properties.theme.font.pages[0]))
    }

    override fun preferredWidth(parentWidth: Int, parentHeight: Int): Int = textWidth
    override fun preferredHeight(parentWidth: Int, parentHeight: Int): Int = textHeight
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

            setUniform("Color", properties.theme.color)
            setUniform("Width", 0.5f)
            setUniform("Edge", 0.1f)

            setUniform("BorderColor", Color.BLACK)
            setUniform("BorderWidth", 0.0f)
            setUniform("BorderEdge", 0.1f)
            setUniform("BorderOffset", Vector2f(0f, 0f))
            mesh.bind {
                mesh.draw()
            }
        }
    }
}