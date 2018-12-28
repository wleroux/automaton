package com.github.wleroux.automaton.component.buttonbase

import com.github.wleroux.automaton.math.Color4f
import com.github.wleroux.keact.api.Node

class ButtonBaseBuilder(private val key: Any? = null) {
    companion object {
        fun buttonBase(key: Any? = null, block: ButtonBaseBuilder.() -> Unit = {}) =
                ButtonBaseBuilder(key).apply(block).build()
    }

    var color: Color4f = Color4f(0.5f, 0.5f, 0.5f, 1f)
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build() =
            Node(ButtonBaseComponent::class, ButtonBaseComponent.ButtonBaseProperties(color, nodes), key)
}