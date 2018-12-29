package com.github.wleroux.automaton.component.button

import com.github.wleroux.automaton.theme.ButtonTheme
import com.github.wleroux.automaton.theme.DEFAULT_THEME
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.event.Event

@NodeBuilderDslMarker
class ButtonBuilder(private val key: Any? = null) {
    companion object {
        fun button(key: Any? = null, block: ButtonBuilder.() -> Unit = {}) =
                ButtonBuilder(key).apply(block).build()
    }

    var disabled: Boolean = false
    var theme: ButtonTheme = DEFAULT_THEME.primaryButtonTheme

    var clickHandler: (Event) -> Unit = {}
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build() =
            Node(ButtonComponent::class, ButtonComponent.ButtonProperties(
                    disabled, theme, clickHandler, nodes
            ), key)
}