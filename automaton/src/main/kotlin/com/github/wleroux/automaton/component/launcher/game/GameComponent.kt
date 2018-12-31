package com.github.wleroux.automaton.component.launcher.game

import com.github.wleroux.automaton.game.GameContext
import com.github.wleroux.automaton.component.launcher.game.event.GameTickedEvent
import com.github.wleroux.automaton.component.launcher.game.menu.GameMenuBuilder.Companion.gameMenu
import com.github.wleroux.automaton.component.launcher.game.viewport.GameViewportBuilder.Companion.gameViewport
import com.github.wleroux.automaton.component.window.event.*
import com.github.wleroux.automaton.game.Game
import com.github.wleroux.automaton.game.Plugin
import com.github.wleroux.automaton.system.worldgenerator.GenerateWorld
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.dispatch
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase
import java.util.*

class GameComponent: Component<GameComponent.GameState, GameComponent.GameProperties>(GameState()) {
    data class GameState(
            val paused: Boolean = false
    )
    data class GameProperties(
            val quitToMainMenu: () -> Unit
    )

    lateinit var game: Game
    lateinit var plugins: List<Plugin>
    override fun componentDidMount() {
        this.dispatch(RequestFocus)
        game = Game()
        plugins = ServiceLoader.load(Plugin::class.java).toList()
        plugins.forEach { it.initialize(game) }

        game.invoke(GenerateWorld)
    }

    override fun componentWillUnmount() {
        plugins.forEach { it.terminate() }
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