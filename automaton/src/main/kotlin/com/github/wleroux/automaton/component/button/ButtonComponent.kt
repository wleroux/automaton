package com.github.wleroux.automaton.component.button

import com.github.wleroux.automaton.component.buttonbase.ButtonBaseBuilder.Companion.buttonBase
import com.github.wleroux.automaton.component.window.event.*
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Padding
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.layout.ItemAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import com.github.wleroux.keact.api.dispatch
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase

class ButtonComponent: Component<ButtonComponent.ButtonState, ButtonComponent.ButtonProperties>(ButtonState()) {
    data class ButtonState(
        val pressed: Boolean = false,
        val hovered: Boolean = false,
        val focused: Boolean = false
    )

    data class ButtonStateProperties(
            val color: Color = Color(0.5f, 0.5f, 0.5f, 1f),
            val border: Border = Border(),
            val padding: Padding = Padding(),
            val margin: Padding = Padding()
    )

    data class ButtonProperties(
            val disabled: Boolean,
            val defaultStyle: ButtonStateProperties,
            val disabledStyle: ButtonStateProperties,
            val hoveredStyle: ButtonStateProperties,
            val pressedStyle: ButtonStateProperties,
            val focusedStyle: ButtonStateProperties,
            val clickHandler: (Event) -> Unit,
            val nodes: List<Node<*, *>>
    )

    override fun asNodes(): List<Node<*, *>> = listOf(
            padding {
                val buttonStyle = when {
                    properties.disabled -> properties.disabledStyle
                    state.pressed -> properties.pressedStyle
                    state.hovered -> properties.hoveredStyle
                    state.focused -> properties.focusedStyle
                    else -> properties.defaultStyle
                }

                padding = buttonStyle.margin
                +buttonBase {
                    color = buttonStyle.color
                    border = buttonStyle.border
                    +padding {
                        padding = buttonStyle.padding
                        +layout {
                            justifyContent = JustifyContent.CENTER
                            alignItems = ItemAlign.CENTER
                            properties.nodes.forEach {
                                +it
                            }
                        }
                    }
                }
            }
    )

    override fun handle(event: Event) {
        if (event.phase != Phase.BUBBLE && event.phase != Phase.TARGET)
            return
        if (properties.disabled)
            return

        when (event.data) {
            is MouseEntered -> {
                state = state.copy(hovered = true)
                event.stopPropagation = true
            }
            is MouseExit -> {
                state = state.copy(hovered = false, pressed = false)
                event.stopPropagation = true
            }
            is MouseClick -> {
                val click = event.data as MouseClick
                state = when (click.action) {
                    MouseAction.PRESSED -> {
                        this.dispatch(RequestFocus)
                        state.copy(pressed = true)
                    }
                    MouseAction.RELEASED -> {
                        properties.clickHandler(event)
                        state.copy(pressed = false)
                    }
                }
                event.stopPropagation = true
            }
            is KeyStroke -> {
                val keyStroke = event.data as KeyStroke
                val execute = (keyStroke.character.toInt() == 28 || keyStroke.character.toInt() == 57)
                if (execute && keyStroke.action == KeyAction.PRESSED) {
                    state = state.copy(pressed = true)
                } else if (execute && keyStroke.action == KeyAction.RELEASED) {
                    properties.clickHandler(event)
                    state = state.copy(pressed = false)
                }
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
            else -> Unit
        }
    }
}