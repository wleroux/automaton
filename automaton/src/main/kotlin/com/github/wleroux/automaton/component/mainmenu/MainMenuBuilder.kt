package com.github.wleroux.automaton.component.mainmenu

import com.github.wleroux.automaton.component.button.ButtonBuilder.Companion.button
import com.github.wleroux.automaton.component.card.CardBuilder.Companion.card
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.component.textfield.TextFieldBuilder.Companion.textField
import com.github.wleroux.automaton.theme.ThemeContext
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.layout.ContentAlign
import com.github.wleroux.keact.api.component.layout.Direction
import com.github.wleroux.keact.api.component.layout.ItemAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import com.github.wleroux.keact.api.theme.Color

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
            justifyContent = JustifyContent.CENTER
            alignContent = ContentAlign.CENTER

            +card {
                theme = contextTheme.primaryCardTheme

                +layout {
                    direction = Direction.COLUMN_REVERSE
                    justifyContent = JustifyContent.CENTER
                    alignContent = ContentAlign.CENTER
                    alignItems = ItemAlign.STRETCH

                    +button {
                        theme = contextTheme.primaryButtonTheme
                        +text {
                            theme = contextTheme.primaryButtonTheme.defaultStyle.textTheme
                            text = "New Game"
                        }
                        clickHandler = {
                            this@MainMenuBuilder.startGameHandler()
                        }
                    }
                    +button {
                        theme = contextTheme.primaryButtonTheme
                        +text {
                            theme = contextTheme.primaryButtonTheme.defaultStyle.textTheme
                            text = "Load Game"
                        }
                        clickHandler = {
                            this@MainMenuBuilder.loadGameHandler()
                        }
                    }
                    +button {
                        theme = contextTheme.primaryButtonTheme
                        disabled = true
                        +text {
                            theme = contextTheme.primaryButtonTheme.defaultStyle.textTheme
                            text = "Settings"
                        }
                        clickHandler = {
                            this@MainMenuBuilder.loadGameHandler()
                        }
                    }
                    +button {
                        theme = contextTheme.secondaryButtonTheme
                        disabled = true
                        +text {
                            theme = contextTheme.secondaryButtonTheme.defaultStyle.textTheme
                            text = "Mods"
                        }
                        clickHandler = {
                            this@MainMenuBuilder.loadGameHandler()
                        }
                    }
                    +button {
                        theme = contextTheme.secondaryButtonTheme
                        +text {
                            theme = contextTheme.primaryButtonTheme.defaultStyle.textTheme
                            text = "Quit"
                        }
                        clickHandler = {
                            this@MainMenuBuilder.quitHandler()
                        }
                    }
                }
            }
        }
    }
}
