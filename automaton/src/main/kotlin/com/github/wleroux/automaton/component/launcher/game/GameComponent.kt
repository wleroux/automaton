package com.github.wleroux.automaton.component.launcher.game

import com.github.wleroux.automaton.component.launcher.game.event.GameTickedEvent
import com.github.wleroux.automaton.component.launcher.game.menu.GameMenuBuilder.Companion.gameMenu
import com.github.wleroux.automaton.component.launcher.game.viewport.GameViewportBuilder.Companion.gameViewport
import com.github.wleroux.automaton.component.window.event.*
import com.github.wleroux.automaton.system.worldgenerator.GenerateWorldCommand
import com.github.wleroux.ecs.api.Game
import com.github.wleroux.automaton.System
import com.github.wleroux.automaton.common.math.Vector2f
import com.github.wleroux.automaton.data.*
import com.github.wleroux.automaton.data.type.CAMERA
import com.github.wleroux.automaton.data.type.KEYBOARD
import com.github.wleroux.automaton.data.type.MOUSE
import com.github.wleroux.automaton.system.worldgenerator.GenerateWorldCommand.TileSettings
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.component.layout.Direction
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.dispatch
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase
import java.util.*
import kotlin.random.Random

class GameComponent: Component<GameComponent.GameState, GameComponent.GameProperties>(GameState()) {
    data class GameState(
            val paused: Boolean = false
    )
    data class GameProperties(
            val quitToMainMenu: () -> Unit
    )

    private lateinit var game: Game
    private lateinit var systems: List<System>
    override fun componentDidMount() {
        this.dispatch(RequestFocus)
        game = Game()
        game[KEYBOARD] = Keyboard()
        game[MOUSE] = Mouse()
        game[CAMERA] = Camera(
                center = Vector2f(0f, 0f),
                rotationAngle = 0f,
                zoomLevel = 50f
        )

        systems = ServiceLoader.load(System::class.java).toList()
        systems.forEach { it.initialize(game) }

        game.invoke(GenerateWorldCommand(
                Random.nextLong(),
                50, 50,
                mapOf(
                        DeciduousTreeTile to TileSettings(
                                0.1f,
                                0.1f
                        ),
                        ConiferousTreeTile to TileSettings(
                                0.3f,
                                0.1f
                        )
                )
        ))
    }

    override fun componentWillUnmount() {
        systems.forEach { it.terminate() }
    }

    override fun asNodes() = listOf(GameContext.provider {
        value = game
        +gameViewport("gameViewport") {
            this@gameViewport.game = this@GameComponent.game
        }
        if (state.paused) {
            +gameMenu("gameMenu") {
                backToGameHandler = {
                    state = state.copy(paused = false)
                    dispatch(RequestFocus)
                }
                quitToMainMenuHandler = properties.quitToMainMenu
            }
        }
    })

    override fun handle(event: Event) {
        if (event.phase == Phase.CAPTURE) return

        when (event.data) {
            is MouseClick -> {
                dispatch(RequestFocus)
                event.stopPropagation = true
            }
            is KeyStroke -> {
                val keyStroke = event.data as KeyStroke
                if (keyStroke.action == KeyAction.RELEASED) {
                    if (keyStroke.character == Keys.ESC) {
                        state = state.copy(paused = true)
                        event.stopPropagation = true
                    }
                }
            }
        }
    }

    private val gameTickEvent = GameTickedEvent(16L)
    override fun render() {
        super.render()
        if (!state.paused) {
            game.publish(gameTickEvent)
        }
    }
}