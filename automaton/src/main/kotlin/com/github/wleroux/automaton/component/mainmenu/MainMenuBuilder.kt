package com.github.wleroux.automaton.component.mainmenu

import com.github.wleroux.automaton.component.button.ButtonBuilder.Companion.button
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.component.theme.ThemeContext
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.layout.ContentAlign
import com.github.wleroux.keact.api.component.layout.Direction
import com.github.wleroux.keact.api.component.layout.ItemAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout

@NodeBuilderDslMarker
class MainMenuBuilder(val key: Any? = null) {
    companion object {
        fun mainMenu(key: Any? = null, block: MainMenuBuilder.() -> Unit = {}) =
                MainMenuBuilder(key).apply(block).build()
    }

    lateinit var startGameHandler: () -> Unit
    lateinit var loadGameHandler: () -> Unit
    lateinit var quitHandler: () -> Unit

    fun build() = ThemeContext.consumer { contextTheme ->
        +layout {
            direction = Direction.COLUMN_REVERSE
            justifyContent = JustifyContent.CENTER
            alignContent = ContentAlign.CENTER
            alignItems = ItemAlign.STRETCH

            +button {
                theme = contextTheme.defaultButtonTheme

                +text {
                    text = "New Game"
                }
                clickHandler = {
                    this@MainMenuBuilder.startGameHandler()
                }
            }
            +button {
                theme = contextTheme.defaultButtonTheme
                +text { text = "Load Game" }
                clickHandler = {
                    this@MainMenuBuilder.loadGameHandler()
                }
            }
            +button {
                theme = contextTheme.defaultButtonTheme
                +text { text = "Quit" }
                clickHandler = {
                    this@MainMenuBuilder.quitHandler()
                }
            }
        }
    }
}
