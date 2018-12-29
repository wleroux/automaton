package com.github.wleroux.automaton.component.mainmenu

import com.github.wleroux.automaton.component.button.ButtonBuilder
import com.github.wleroux.automaton.component.button.ButtonBuilder.Companion.button
import com.github.wleroux.automaton.component.text.TextBuilder
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.keact.api.component.layout.ContentAlign
import com.github.wleroux.keact.api.component.layout.Direction
import com.github.wleroux.keact.api.component.layout.ItemAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout

class MainMenuBuilder(val key: Any? = null) {
    companion object {
        fun mainMenu(key: Any? = null, block: MainMenuBuilder.() -> Unit = {}) =
                MainMenuBuilder(key).apply(block).build()
    }

    lateinit var startGameHandler: () -> Unit
    lateinit var loadGameHandler: () -> Unit
    lateinit var quitHandler: () -> Unit

    fun build() = layout {
        direction = Direction.COLUMN_REVERSE
        justifyContent = JustifyContent.CENTER
        alignContent = ContentAlign.CENTER
        alignItems = ItemAlign.STRETCH

        +button {
            +text { text = "New Game" }
            clickHandler = {
                startGameHandler()
            }
        }
        +button {
            +text { text = "Load Game" }
            clickHandler = {
                loadGameHandler()
            }
        }
        +button {
            +text { text = "Quit" }
            clickHandler = {
                quitHandler()
            }
        }
    }
}
