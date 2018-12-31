package com.github.wleroux.automaton.component.textfield

import com.github.wleroux.automaton.component.card.CardBuilder.Companion.card
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.component.text.font.FontMeshCreator
import com.github.wleroux.automaton.component.window.event.*
import com.github.wleroux.automaton.component.window.event.Keys.BACKSPACE
import com.github.wleroux.automaton.component.window.event.Keys.DELETE
import com.github.wleroux.automaton.component.window.event.Keys.LEFT
import com.github.wleroux.automaton.component.window.event.Keys.RIGHT
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.dispatch
import com.github.wleroux.keact.api.event.Event
import kotlin.math.max
import kotlin.math.min

class TextFieldComponent: Component<TextFieldComponent.TextFieldState, TextFieldComponent.TextFieldProperties>(TextFieldState()) {
    data class TextFieldProperties(
            val theme: TextFieldTheme,
            val initialText: String = "",
            val placeholderText: String = "",
            val textChangeHandler: (String) -> Boolean = { true },
            val disabled: Boolean = false
    )
    data class TextFieldState(
            val text: String = "",
            val cursorPosition: Int = 0,
            val hovered: Boolean = false,
            val focused: Boolean = false
    )

    override fun componentDidMount() {
        state = state.copy(
                text = properties.initialText,
                cursorPosition = properties.initialText.length
        )
    }

    override fun asNodes() = listOf(card {
        val style = when {
            properties.disabled -> properties.theme.disabledStyle
            state.focused -> properties.theme.focusedStyle
            state.hovered -> properties.theme.hoveredStyle
            else -> properties.theme.defaultStyle
        }

        theme = style.cardTheme
        +text {
            theme = when {
                state.text.isEmpty() -> style.placeholderTextTheme
                else -> style.textTheme
            }
            val displayText = if (state.text.isEmpty()) {
                properties.placeholderText
            } else {
                state.text
            }

            text = if (state.focused) {
                displayText.substring(0, state.cursorPosition) + "|" + displayText.substring(state.cursorPosition)
            } else {
                displayText
            }
        }
    })

    override fun handle(event: Event) {
        if (properties.disabled)
            return

        when (event.data) {
            is MouseEntered -> {
                state = state.copy(
                        hovered = true
                )
                event.stopPropagation = true
            }
            is MouseExit -> {
                state = state.copy(
                        hovered = false
                )
                event.stopPropagation = true
            }
            is MouseClick -> {
                val mouseClick = event.data as MouseClick
                if (mouseClick.action != MouseAction.RELEASED) return

                if (!state.focused) {
                    this.dispatch(RequestFocus)
                }

                // Set cursor position to mouse click location
                val relativeX = mouseClick.mouseX - x
                val relativeY = mouseClick.mouseY - y
                val style = when {
                    properties.disabled -> properties.theme.disabledStyle
                    state.focused -> properties.theme.focusedStyle
                    state.hovered -> properties.theme.hoveredStyle
                    else -> properties.theme.defaultStyle
                }
                val displayText = if (state.focused) {
                    state.text.substring(0, state.cursorPosition) + "|" + state.text.substring(state.cursorPosition)
                } else {
                    state.text
                }
                val mouseCursorLocation = FontMeshCreator.getCursorPosition(relativeX, relativeY, displayText, style.textTheme.font, style.textTheme.size)
                state = state.copy(
                        cursorPosition = if (mouseCursorLocation > state.cursorPosition) {
                            mouseCursorLocation - 1
                        } else {
                            mouseCursorLocation
                        }
                )

                event.stopPropagation = true
            }
            is Focus -> {
                state = state.copy(focused = true)
                event.stopPropagation = true
            }
            is Unfocus -> {
                state = state.copy(focused = false)
                event.stopPropagation = true
            }
            is KeyStroke -> {
                val keyStroke = event.data as KeyStroke
                if (keyStroke.action == KeyAction.TYPED) {
                    val newText = state.text.substring(0, state.cursorPosition) + keyStroke.character + state.text.substring(state.cursorPosition)
                    if (properties.textChangeHandler.invoke(newText)) {
                        state = state.copy(text = newText, cursorPosition = state.cursorPosition + 1)
                    }
                    event.stopPropagation = true
                } else if (keyStroke.action == KeyAction.PRESSED) {
                    when (keyStroke.character) {
                        RIGHT -> {
                            state = state.copy(
                                    cursorPosition = min(state.text.length, state.cursorPosition + 1)
                            )
                            event.stopPropagation = true
                        }
                        LEFT -> {
                            state = state.copy(
                                    cursorPosition = max(state.cursorPosition - 1, 0)
                            )
                            event.stopPropagation = true
                        }
                        DELETE -> {
                            if (state.cursorPosition < state.text.length) {
                                val newText = state.text.substring(0, state.cursorPosition) + state.text.substring(state.cursorPosition + 1)
                                if (properties.textChangeHandler.invoke(newText)) {
                                        state = state.copy(text = newText)
                                }
                            }
                            event.stopPropagation = true
                        }
                        BACKSPACE -> {
                            if (state.cursorPosition > 0) {
                                val newText = state.text.substring(0, state.cursorPosition - 1) + state.text.substring(state.cursorPosition)
                                if (properties.textChangeHandler.invoke(newText)) {
                                    state = state.copy(text = newText, cursorPosition = max(state.cursorPosition - 1, 0))
                                }
                            }
                            event.stopPropagation = true
                        }
                   }
                }
            }
        }
    }
}