package com.github.wleroux.automaton.component.launcher.game.viewport

import com.github.wleroux.automaton.data.POSITION
import com.github.wleroux.automaton.data.RENDERABLE
import com.github.wleroux.automaton.loadMesh
import com.github.wleroux.automaton.loadText
import com.github.wleroux.automaton.loadTexture
import com.github.wleroux.automaton.common.math.Matrix4f
import com.github.wleroux.automaton.common.math.Quaternion
import com.github.wleroux.automaton.common.math.Vector3f
import com.github.wleroux.automaton.common.program.Material
import com.github.wleroux.automaton.common.program.Model
import com.github.wleroux.automaton.common.program.Program
import com.github.wleroux.ecs.api.Game
import com.github.wleroux.keact.api.Component
import org.lwjgl.opengl.GL11

class GameViewportComponent: Component<Unit, Game>(Unit) {
    private lateinit var program: Program
    private lateinit var model: Model
    override fun componentWillMount() {
        program = Program.build {
            vertexShader(loadText("com/github/wleroux/automaton/common/program/shader/standard.vs.glsl"))
            uniform("model", "view", "projection")
            fragmentShader(loadText("com/github/wleroux/automaton/common/program/shader/standard.fs.glsl"))
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

            val cameraPosition = Vector3f(0f, 50f, -10f)
            val cameraRotation = Quaternion(Vector3f.AXIS_X, Math.PI.toFloat() * (7f / 16f))
            val view = Matrix4f(position = cameraPosition, rotation = cameraRotation).invert()
            val projection = Matrix4f.perspective(Math.PI.toFloat() / 8f, width, height, 0.03f, 1000f)
            setUniform("projection", projection)
            setUniform("view", view)
            val positions = properties[POSITION]
            properties[RENDERABLE].forEach { (entityId, model) ->
                positions[entityId]?.let { position ->
                    setUniform("model", Matrix4f(Vector3f(position.x, 0f, position.y)))
                    setUniform("baseColorTexture", model.material.baseColorTexture)
                    model.mesh.bind { draw() }
                }
            }
        }
    }
}
