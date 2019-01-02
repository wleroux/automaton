package com.github.wleroux.automaton.component.launcher.game.viewport

import com.github.wleroux.automaton.loadMesh
import com.github.wleroux.automaton.loadText
import com.github.wleroux.automaton.loadTexture
import com.github.wleroux.automaton.common.math.Matrix4f
import com.github.wleroux.automaton.common.math.Quaternion
import com.github.wleroux.automaton.common.math.Vector3f
import com.github.wleroux.automaton.common.math.perlin_noise
import com.github.wleroux.automaton.common.program.Material
import com.github.wleroux.automaton.common.program.Model
import com.github.wleroux.automaton.common.program.Program
import com.github.wleroux.automaton.data.TRANSFORM
import com.github.wleroux.automaton.data.ConiferousTreeTile
import com.github.wleroux.automaton.data.DeciduousTreeTile
import com.github.wleroux.automaton.data.TILES
import com.github.wleroux.ecs.api.Game
import com.github.wleroux.keact.api.Component
import org.lwjgl.opengl.GL11.*
import kotlin.math.PI
import kotlin.math.sqrt

class GameViewportComponent: Component<Unit, Game>(Unit) {
    private lateinit var program: Program
    private lateinit var coniferousTreeModel: Model
    private lateinit var deciduousTreeModel: Model
    private lateinit var rockModel: Model
    override fun componentWillMount() {
        program = Program.build {
            vertexShader(loadText("com/github/wleroux/automaton/common/program/standard/standard.vs.glsl"))
            uniform("model", "view", "projection")
            fragmentShader(loadText("com/github/wleroux/automaton/common/program/standard/standard.fs.glsl"))
            uniform("baseColorTexture")
        }

        coniferousTreeModel = Model(
                loadMesh("automaton/asset/tree_coniferous.obj"),
                Material(loadTexture("automaton/asset/tree_coniferous_diff_1k.png"))
        )
        deciduousTreeModel = Model(
                loadMesh("automaton/asset/tree_deciduous.obj"),
                Material(loadTexture("automaton/asset/tree_deciduous_diff_1k.png"))
        )
        rockModel = Model(
                loadMesh("automaton/asset/rock.obj"),
                Material(loadTexture("automaton/asset/rock_diff_1k.png"))
        )
    }

    override fun preferredWidth(parentWidth: Int, parentHeight: Int) = parentWidth
    override fun preferredHeight(parentWidth: Int, parentHeight: Int) = parentHeight

    override fun render() {
        program.use {
            // Set background color
            glEnable(GL_SCISSOR_TEST)
            glScissor(0, 0, width, height)
            glViewport(x, y, width, height)
            glClearColor(0.1f, 0.3f, 0.1f, 0.0f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glDisable(GL_SCISSOR_TEST)

            val cameraPosition = Vector3f(25f, 30f, -5f)
            val cameraRotation = Quaternion(Vector3f.AXIS_X, Math.PI.toFloat() * (4f / 16f))
            val view = Matrix4f(position = cameraPosition, rotation = cameraRotation).invert()
            val projection = Matrix4f.perspective(Math.PI.toFloat() / 8f, width, height, 0.03f, 1000f)
            setUniform("projection", projection)
            setUniform("view", view)

            val tiles = properties[TILES]
            tiles.forEach { x, z ->
                val tile = tiles[x, z]

                when (tile) {
                    ConiferousTreeTile -> {
                        setUniform("baseColorTexture", coniferousTreeModel.material.baseColorTexture)
                        val scale = Vector3f(sqrt(2f)/2f, sqrt(2f)/2f, sqrt(2f)/2f)
                        val blueX = perlin_noise(0L, x.toFloat(), 0f, z.toFloat()) * 0.25f
                        val blueZ = perlin_noise(1L, x.toFloat(), 0f, z.toFloat()) * 0.25f

                        coniferousTreeModel.mesh.bind {
                            setUniform("model", Matrix4f(
                                    position = Vector3f(blueX + x.toFloat(), 0f, blueZ + z.toFloat()),
                                    scale = scale,
                                    rotation = Quaternion(Vector3f.AXIS_Y, perlin_noise(2L, x.toFloat(), 0f, z.toFloat()) * 2f * PI.toFloat())
                            ))
                            draw()

                            var count = 0
                            if (tiles[x + 1, z] != null) count ++
                            if (tiles[x + 1, z + 1] != null) count ++
                            if (tiles[x, z + 1] != null) count ++
                            if (count == 3) {
                                setUniform("model", Matrix4f(
                                        position = Vector3f(x.toFloat() + blueX + 0.5f, 0f, z.toFloat() + blueZ + 0.5f),
                                        scale = scale,
                                        rotation = Quaternion(Vector3f.AXIS_Y, perlin_noise(3L, x.toFloat(), 0f, z.toFloat()) * 2f * PI.toFloat())
                                ))
                                draw()
                            }
                        }
                    }
                    DeciduousTreeTile -> {
                        setUniform("baseColorTexture", deciduousTreeModel.material.baseColorTexture)
                        val blueX = perlin_noise(0L, x.toFloat(), 0f, z.toFloat()) * 0.1f
                        val blueZ = perlin_noise(0L, x.toFloat(), 0f, z.toFloat()) * 0.1f
                        val scale = Vector3f(sqrt(2f)/2f, sqrt(2f)/2f, sqrt(2f)/2f)
                        deciduousTreeModel.mesh.bind {
                            setUniform("model", Matrix4f(
                                    position = Vector3f(blueX + x.toFloat(), 0f, blueZ + z.toFloat()),
                                    scale = scale,
                                    rotation = Quaternion(Vector3f.AXIS_Y, perlin_noise(2L, x.toFloat(), 0f, z.toFloat()) * 2f * PI.toFloat())
                            ))
                            draw()

                            var count = 0
                            if (tiles[x + 1, z] != null) count ++
                            if (tiles[x + 1, z + 1] != null) count ++
                            if (tiles[x, z + 1] != null) count ++
                            if (count == 3) {
                                setUniform("model", Matrix4f(
                                        position = Vector3f(x.toFloat() + blueX + 0.5f, 0f, z.toFloat() + blueZ + 0.5f),
                                        scale = scale,
                                        rotation = Quaternion(Vector3f.AXIS_Y, perlin_noise(3L, x.toFloat(), 0f, z.toFloat()) * 2f * PI.toFloat())
                                ))
                                draw()
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
