package com.github.wleroux.automaton.component.button

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.theme.Color

class ButtonBuilder(private val key: Any? = null) {
    companion object {
        fun button(key: Any? = null, block: ButtonBuilder.() -> Unit = {}) =
                ButtonBuilder(key).apply(block).build()
    }

    var disabled: Boolean = false
    var defaultStyle: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = Color(0.5f, 0.5f, 0.5f, 1f))
    var disabledStyle: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = Color(0.1f, 0.1f, 0.1f, 1f))
    var hoveredStyle: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = Color(0.7f, 0.7f, 0.7f, 1f))
    var pressedStyle: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = Color(0.3f, 0.3f, 0.3f, 1f))
    var focusedStyle: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = Color(0.8f, 0.8f, 0.8f, 1f))

    var clickHandler: (Event) -> Unit = {}
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build() =
            Node(ButtonComponent::class, ButtonComponent.ButtonProperties(
                    disabled, defaultStyle, disabledStyle, hoveredStyle, pressedStyle, focusedStyle, clickHandler, nodes
            ), key)
}