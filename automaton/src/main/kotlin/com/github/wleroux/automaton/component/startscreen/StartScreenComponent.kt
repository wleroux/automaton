package com.github.wleroux.automaton.component.startscreen

import com.github.wleroux.automaton.component.button.ButtonBuilder.Companion.button
import com.github.wleroux.automaton.component.card.CardBuilder.Companion.card
import com.github.wleroux.automaton.component.mainmenu.MainMenuBuilder
import com.github.wleroux.automaton.component.mainmenu.MainMenuBuilder.Companion.mainMenu
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.component.textfield.TextFieldBuilder.Companion.textField
import com.github.wleroux.automaton.theme.ThemeContext
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.layout.ContentAlign
import com.github.wleroux.keact.api.component.layout.Direction
import com.github.wleroux.keact.api.component.layout.ItemAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout

fun startScreen(key: Any? = null)
    = Node(StartScreenComponent::class, Unit, key)

class StartScreenComponent: Component<String, Unit>("") {
    override fun asNodes() = listOf(ThemeContext.consumer { themeContext ->
        +layout {
            justifyContent = JustifyContent.CENTER
            alignContent = ContentAlign.CENTER
            +card {
                theme = themeContext.primaryCardTheme
                +layout {
                    direction = Direction.COLUMN_REVERSE
                    alignItems = ItemAlign.STRETCH
                    +textField {
                        theme = themeContext.primaryTextFieldTheme
                        textChangeHandler = { text ->
                            state = text
                            true
                        }
                    }
                    +text {
                        theme = themeContext.primaryButtonTheme.defaultStyle.textTheme
                        text = state
                    }
                    +button {
                        theme = themeContext.primaryButtonTheme
                        +text {
                            theme = themeContext.primaryButtonTheme.defaultStyle.textTheme
                            text = state
                        }
                    }

                    +mainMenu {
                        quitHandler = {
                            System.exit(1)
                        }
                        startGameHandler = {
                            println("Start Game")
                        }
                        loadGameHandler = {
                            println("Load Game")
                        }
                    }
                }
            }
        }
    })
}