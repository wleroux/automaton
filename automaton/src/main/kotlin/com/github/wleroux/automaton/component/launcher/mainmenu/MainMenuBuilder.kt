    package com.github.wleroux.automaton.component.launcher.mainmenu

import com.github.wleroux.automaton.component.button.ButtonBuilder.Companion.button
import com.github.wleroux.automaton.component.card.CardBuilder.Companion.card
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.component.launcher.ThemeContext
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout

@NodeBuilderDslMarker
class MainMenuBuilder(val key: Any? = null) {
    companion object {
        fun mainMenu(key: Any? = null, block: MainMenuBuilder.() -> Unit = {}) =
                MainMenuBuilder(key).apply(block).build()
    }

    lateinit var newGameHandler: () -> Unit
    lateinit var quitHandler: () -> Unit

    fun build() = ThemeContext.consumer { contextTheme ->
        +card {
            theme = contextTheme.primaryCardTheme

            +layout {
                +text {
                    theme = contextTheme.primaryCardTextTheme
                    text = "Main Menu"
                }

                +button {
                    theme = contextTheme.primaryButtonTheme
                    +text {
                        theme = contextTheme.primaryButtonTheme.defaultStyle.textTheme
                        text = "New Game"
                    }
                    clickHandler = {
                        this@MainMenuBuilder.newGameHandler()
                    }
                }

                +button {
                    theme = contextTheme.secondaryButtonTheme
                    +text {
                        theme = contextTheme.primaryButtonTheme.defaultStyle.textTheme
                        text = "Quit to Desktop"
                    }
                    clickHandler = {
                        this@MainMenuBuilder.quitHandler()
                    }
                }
            }
        }
    }
}
