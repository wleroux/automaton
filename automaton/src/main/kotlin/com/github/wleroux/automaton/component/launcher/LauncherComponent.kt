package com.github.wleroux.automaton.component.launcher

import com.github.wleroux.automaton.component.launcher.fpscounter.FPSCounterBuilder.Companion.fpsCounter
import com.github.wleroux.automaton.component.launcher.game.GameBuilder.Companion.game
import com.github.wleroux.automaton.component.launcher.mainmenu.MainMenuBuilder.Companion.mainMenu
import com.github.wleroux.automaton.component.window.event.QuitToDesktop
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.layout.ContentAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.component.overlay.OverlayBuilder.Companion.overlay
import com.github.wleroux.keact.api.dispatch

class LauncherComponent: Component<LauncherComponent.LaunchState, Unit>(LaunchState()) {
    data class LaunchState(
            val gameCreated: Boolean = false
    )

    override fun asNodes(): List<Node<*, *>> = listOf(overlay {
        if (state.gameCreated) {
            +game {
                quitToMainMenuHandler = {
                    state = state.copy(gameCreated = false)
                }
            }
        } else {
            +layout {
                justifyContent = JustifyContent.CENTER
                alignContent = ContentAlign.CENTER
                +mainMenu {
                    newGameHandler = {
                        state = state.copy(
                                gameCreated = true
                        )
                    }
                    quitHandler = {
                        dispatch(QuitToDesktop)
                    }
                }
            }
        }
        +fpsCounter("fpsCounter")
    })
}