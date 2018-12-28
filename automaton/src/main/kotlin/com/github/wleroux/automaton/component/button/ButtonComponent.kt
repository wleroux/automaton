package com.github.wleroux.automaton.component.button

import com.github.wleroux.automaton.component.buttonbase.ButtonBaseBuilder.Companion.buttonBase
import com.github.wleroux.automaton.component.window.event.MouseAction
import com.github.wleroux.automaton.component.window.event.MouseClick
import com.github.wleroux.automaton.component.window.event.MouseEntered
import com.github.wleroux.automaton.component.window.event.MouseExit
import com.github.wleroux.automaton.math.Color4f
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.layout.ItemAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase
import org.lwjgl.glfw.GLFW

class ButtonComponent: Component<ButtonComponent.ButtonState, ButtonComponent.ButtonProperties>() {
    enum class ButtonState {
        DEFAULT,
        HOVERED,
        DISABLED,
        PRESSED,
        FOCUSED
    }
    data class ButtonProperties(
        val defaultColor: Color4f,
        val disabledColor: Color4f,
        val hoveredColor: Color4f,
        val pressedColor: Color4f,
        val focusedColor: Color4f,
        val clickHandler: (Event) -> Unit,
        val nodes: List<Node<*, *>>
    )

    override lateinit var properties: ButtonProperties
    override var state: ButtonState = ButtonState.DEFAULT

    override fun asNodes(): List<Node<*, *>> = listOf(
            buttonBase {
                color = when (state) {
                    ButtonState.DEFAULT -> properties.defaultColor
                    ButtonState.HOVERED -> properties.hoveredColor
                    ButtonState.PRESSED -> properties.pressedColor
                    ButtonState.FOCUSED -> properties.focusedColor
                    ButtonState.DISABLED -> properties.disabledColor
                }

                +layout {
                    justifyContent = JustifyContent.CENTER
                    alignItems = ItemAlign.CENTER
                    properties.nodes.forEach {
                        +it
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