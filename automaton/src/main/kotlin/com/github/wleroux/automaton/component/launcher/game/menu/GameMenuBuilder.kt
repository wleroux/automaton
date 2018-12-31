package com.github.wleroux.automaton.component.launcher.game.menu

import com.github.wleroux.automaton.component.button.ButtonBuilder.Companion.button
import com.github.wleroux.automaton.component.modal.ModalBuilder.Companion.modal
import com.github.wleroux.automaton.component.surface.SurfaceBuilder.Companion.surface
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.theme.ThemeContext
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout

class GameMenuBuilder(private val key: Any? = null) {
    companion object {
        fun gameMenu(key: Any? = null, block: GameMenuBuilder.() -> Unit = {}) =
                GameMenuBuilder(key).apply(block).build()
    }

    lateinit var backToGameHandler: () -> Unit
    lateinit var quitToMainMenuHandler: () -> Unit

    fun build() = ThemeContext.consumer(key) { themeContext ->
        +modal {
            theme = themeContext.modalCardTheme
            cancelModalHandler = backToGameHandler
            +surface {
                button {  }
                theme = themeContext.primaryCardTheme
                +layout {
                    +text {
                        theme = themeContext.primaryCardTextTheme
                        text = "Game Menu"
                    }

                    +button {
                        theme = themeContext.primaryButtonTheme
                        +text {
                            theme = themeContext.primaryButtonTheme.defaultStyle.textTheme
                            text = "Back to Game"
                        }
                        clickHandler = {backToGameHandler()}
                    }

                    +button {
                        theme = themeContext.secondaryButtonTheme
                        +text {
                            theme = themeContext.primaryButtonTheme.defaultStyle.textTheme
                            text = "Quit to Main Menu"
                        }
                        clickHandler = {quitToMainMenuHandler()}
                    }
                }
            }
        }
    }
}