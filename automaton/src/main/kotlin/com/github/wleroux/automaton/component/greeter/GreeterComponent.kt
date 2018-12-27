package com.github.wleroux.automaton.component.greeter

import com.github.wleroux.automaton.component.Components.text
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node

class GreeterComponent: Component<Unit, List<Node<*, *>>>() {
    override var properties: List<Node<*, *>> = emptyList()
    override var state: Unit = Unit
    override fun asNodes(): List<Node<*, *>> = listOf(
        text { +"Hello, " },
        *properties.toTypedArray(),
        text { +"!" },
        text { +System.lineSeparator()}
    )
}