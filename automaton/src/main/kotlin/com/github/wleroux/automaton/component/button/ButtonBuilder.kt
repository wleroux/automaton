package com.github.wleroux.automaton.component.button

import com.github.wleroux.automaton.math.Color4f
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.event.Event

class ButtonBuilder(private val key: Any? = null) {
    companion object {
        fun button(key: Any? = null, block: ButtonBuilder.() -> Unit = {}) =
                ButtonBuilder(key).apply(block).build()
    }

    var defaultColor: Color4f = Color4f(0.5f, 0.5f, 0.5f, 1f)
    var disabledColor: Color4f = defaultColor
    var hoveredColor: Color4f =  Color4f(0.7f, 0.7f, 0.7f, 1f)
    var pressedColor: Color4f  =  Color4f(0.3f, 0.3f, 0.3f, 1f)
    var focusedColor: Color4f = defaultColor

    var clickHandler: (Event) -> Unit = {}
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build() =
            Node(ButtonComponent::class, ButtonComponent.ButtonProperties(
                    defaultColor, disabledColor, hoveredColor, pressedColor, focusedColor, clickHandler, nodes
            ), key)
}