package com.github.wleroux.automaton.component.launcher.game.viewport

import com.github.wleroux.automaton.game.Game
import com.github.wleroux.automaton.game.data.PositionData
import com.github.wleroux.automaton.game.data.RenderableData
import com.github.wleroux.automaton.loadMesh
import com.github.wleroux.automaton.loadText
import com.github.wleroux.automaton.loadTexture
import com.github.wleroux.automaton.math.Matrix4f
import com.github.wleroux.automaton.math.Quaternion
import com.github.wleroux.automaton.math.Vector3f
import com.github.wleroux.automaton.program.Material
import com.github.wleroux.automaton.program.Model
import com.github.wleroux.automaton.program.Program
import com.github.wleroux.keact.api.Component
import org.lwjgl.opengl.GL11

class GameViewportComponent: Component<Unit, Game>(Unit) {
    private lateinit var program: Program
    private lateinit var model: Model
    override fun componentWillMount() {
        program = Program.build {
            vertexShader(loadText("automaton/shader/standard.vs.glsl"))
            uniform("model", "view", "projection")
            fragmentShader(loadText("automaton/shader/standard.fs.glsl"))
            uniform("baseColorTexture")
        }

        model = Model(
                loadMesh("automaton/asset/cube.obj"),
                Material(
                        loadTexture("automaton/asset/Color.png")
                )
        )
    }

    override fun preferredWidth(parentWidth: Int, parentHeight: Int) = parentWidth
    override fun preferredHeight(parentWidth: Int, parentHeight: Int) = parentHeight

    override fun render() {
        program.use {
            GL11.glViewport(x, y, width, height)
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT)

            val cameraPosition = Vector3f(0f, 10f, -2f)
            val cameraRotation = Quaternion(Vector3f.AXIS_X, Math.PI.toFloat() * (7f / 16f))
            val view = Matrix4f(position = cameraPosition, rotation = cameraRotation).invert()
            val projection = Matrix4f.perspective(Math.PI.toFloat() / 8f, width, height, 0.03f, 1000f)
            setUniform("projection", projection)
            setUniform("view", view)
            val positions = properties[PositionData::class]
            properties[RenderableData::class].forEach { entityId, renderableData ->
                positions[entityId]?.let { positionData ->
                    setUniform("model", Matrix4f(Vector3f(positionData.position.x, 0f, positionData.position.y)))
                    setUniform("baseColorTexture", renderableData.texture)
                    renderableData.mesh.bind { draw() }
                }
            }
        }
    }
}
