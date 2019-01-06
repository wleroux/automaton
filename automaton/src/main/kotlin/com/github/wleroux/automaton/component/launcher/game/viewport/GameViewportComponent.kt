package com.github.wleroux.automaton.component.launcher.game.viewport

import com.github.wleroux.automaton.common.math.*
import com.github.wleroux.automaton.loadMesh
import com.github.wleroux.automaton.loadText
import com.github.wleroux.automaton.loadTexture
import com.github.wleroux.automaton.common.program.Material
import com.github.wleroux.automaton.common.program.Model
import com.github.wleroux.automaton.common.program.Program
import com.github.wleroux.automaton.component.window.event.*
import com.github.wleroux.automaton.data.*
import com.github.wleroux.automaton.data.type.TILES
import com.github.wleroux.automaton.data.type.CAMERA
import com.github.wleroux.automaton.data.type.KEYBOARD
import com.github.wleroux.automaton.data.type.MOUSE
import com.github.wleroux.automaton.system.treeplanter.ActionCommand
import com.github.wleroux.ecs.api.Game
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.dispatch
import com.github.wleroux.keact.api.event.Event
import org.lwjgl.opengl.GL11.*
import kotlin.math.PI
import kotlin.math.sqrt

class GameViewportComponent: Component<Unit, Game>(Unit) {
    private lateinit var program: Program
    private lateinit var coniferousTreeModel: Model
    private lateinit var deciduousTreeModel: Model
    private lateinit var rockModel: Model
    private var focused: Boolean = false
    override fun componentDidMount() {
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


    lateinit var view: Matrix4f
    lateinit var projection: Matrix4f
    override fun render() {
        program.use {
            // Set background color
            glEnable(GL_SCISSOR_TEST)
            glScissor(x, y, width, height)
            glViewport(x, y, width, height)
            glClearColor(0.1f, 0.3f, 0.1f, 0.0f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glDisable(GL_SCISSOR_TEST)

            view = Matrix4f(properties[CAMERA].toTransform()).invert()
            projection = Matrix4f.perspective(Math.PI.toFloat() / 8f, width, height, 0.03f, 1000f)
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

    override fun handle(event: Event) {
        properties[KEYBOARD].handle(event)
        properties[MOUSE].handle(event)

        when (event.data) {
            is Focus -> {
                focused = true
                event.stopPropagation = true
            }
            is Unfocus -> {
                focused = false
                event.stopPropagation = true
            }
            is MouseClick -> {
                val mouseClick = event.data as MouseClick
                if (mouseClick.action == MouseAction.RELEASED) {
                    if (!focused) {
                        this.dispatch(RequestFocus)
                    } else {
                        val screenX = 2f * ((mouseClick.mouseX.toFloat() - x.toFloat()) / width.toFloat()) - 1f
                        val screenY = 2f * ((mouseClick.mouseY.toFloat() - y.toFloat()) / height.toFloat()) - 1f

                        val screenPosFar = Vector4f(screenX, screenY, 1f, 1f)
                        var worldPosFar = screenPosFar * projection.invert() * view.invert()
                        worldPosFar *= (1f / worldPosFar.w)

                        val screenPosNear = Vector4f(screenX, screenY, -1f, 1f)
                        var worldPosNear = screenPosNear * projection.invert() * view.invert()
                        worldPosNear *= (1f / worldPosNear.w)

                        properties.invoke(ActionCommand(
                                Vector3f(worldPosNear.x, worldPosNear.y, worldPosNear.z),
                                Vector3f(worldPosFar.x, worldPosFar.y, worldPosFar.z)
                        ))
                    }
                }
                event.stopPropagation = true
            }
        }
    }
}
