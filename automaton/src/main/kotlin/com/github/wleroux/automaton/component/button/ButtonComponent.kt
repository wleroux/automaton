package com.github.wleroux.automaton.component.button

import com.github.wleroux.automaton.component.buttonbase.ButtonBaseBuilder.Companion.buttonBase
import com.github.wleroux.automaton.component.window.event.MouseAction
import com.github.wleroux.automaton.component.window.event.MouseClick
import com.github.wleroux.automaton.component.window.event.MouseEntered
import com.github.wleroux.automaton.component.window.event.MouseExit
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Padding
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.layout.ItemAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase

class ButtonComponent: Component<ButtonComponent.ButtonState, ButtonComponent.ButtonProperties>() {
    enum class ButtonState {
        DEFAULT,
        HOVERED,
        DISABLED,
        PRESSED,
        FOCUSED
    }
    data class ButtonStateProperties(
            val color: Color = Color(0.5f, 0.5f, 0.5f, 1f),
            val border: Border = Border(),
            val padding: Padding = Padding(),
            val margin: Padding = Padding()
    )

    data class ButtonProperties(
        val default: ButtonStateProperties,
        val disabled: ButtonStateProperties,
        val hovered: ButtonStateProperties,
        val pressed: ButtonStateProperties,
        val focused: ButtonStateProperties,
        val clickHandler: (Event) -> Unit,
        val nodes: List<Node<*, *>>
    )

    override lateinit var properties: ButtonProperties
    override var state: ButtonState = ButtonState.DEFAULT

    override fun asNodes(): List<Node<*, *>> = listOf(
            padding {
                val buttonState = when (state) {
                    ButtonState.DEFAULT -> properties.default
                    ButtonState.HOVERED -> properties.hovered
                    ButtonState.PRESSED -> properties.pressed
                    ButtonState.FOCUSED -> properties.focused
                    ButtonState.DISABLED -> properties.disabled
                }

                padding = buttonState.padding
                +buttonBase {
                    color = buttonState.color
                    border = buttonState.border
                    +padding {
                        padding = buttonState.padding
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
        if (event.phase != Phase.BUBBLE)
            return

        when (event.data) {
            is MouseEntered -> nextState = ButtonState.HOVERED
            is MouseExit -> nextState = ButtonState.DEFAULT
            is MouseClick -> {
                val click = event.data as MouseClick
                nextState = when (click.action) {
                    MouseAction.PRESSED -> ButtonState.PRESSED
                    MouseAction.RELEASED -> {
                        properties.clickHandler(event)
                        ButtonState.DEFAULT
                    }
                }
            }
            else -> Unit
        }
    }
}