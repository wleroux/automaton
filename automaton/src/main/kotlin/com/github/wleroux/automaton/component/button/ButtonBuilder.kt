package com.github.wleroux.automaton.component.button

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.theme.Color

class ButtonBuilder(private val key: Any? = null) {
    companion object {
        fun button(key: Any? = null, block: ButtonBuilder.() -> Unit = {}) =
                ButtonBuilder(key).apply(block).build()
    }

    var default: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = Color(0.5f, 0.5f, 0.5f, 1f))
    var disabled: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = default.color)
    var hovered: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = Color(0.7f, 0.7f, 0.7f, 1f))
    var pressed: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = Color(0.3f, 0.3f, 0.3f, 1f))
    var focused: ButtonComponent.ButtonStateProperties = ButtonComponent.ButtonStateProperties(color = default.color)

    var clickHandler: (Event) -> Unit = {}
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build() =
            Node(ButtonComponent::class, ButtonComponent.ButtonProperties(
                    default, disabled, hovered, pressed, focused, clickHandler, nodes
            ), key)
}