package com.github.wleroux.automaton.component.launcher.game

import com.github.wleroux.automaton.game.GameContext
import com.github.wleroux.automaton.component.launcher.game.event.GameTickedEvent
import com.github.wleroux.automaton.component.launcher.game.menu.GameMenuBuilder.Companion.gameMenu
import com.github.wleroux.automaton.component.launcher.game.viewport.GameViewportBuilder.Companion.gameViewport
import com.github.wleroux.automaton.component.window.event.*
import com.github.wleroux.automaton.game.Game
import com.github.wleroux.automaton.game.Plugin
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.component.overlay.OverlayBuilder.Companion.overlay
import com.github.wleroux.keact.api.dispatch
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase
import java.util.*

class GameComponent: Component<GameComponent.GameState, GameComponent.GameProperties>(GameState()) {
    data class GameState(
            val optionsOpened: Boolean = false
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
    }

    override fun componentWillUnmount() {
        plugins.forEach { it.terminate() }
    }

    override fun asNodes() = listOf(GameContext.provider {
        value = game
        +gameViewport("gameViewport")
        if (state.optionsOpened) {
            +gameMenu("gameMenu") {
                backToGameHandler = {
                    state = state.copy(optionsOpened = false)
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
                        state = state.copy(optionsOpened = true)
                        event.stopPropagation = true
                    }
                }
            }
        }
    }

    private val gameTickEvent = GameTickedEvent(16L)
    override fun render() {
        super.render()
        game.publish(gameTickEvent)
    }
}