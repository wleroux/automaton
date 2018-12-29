package com.github.wleroux.automaton.component.buttonbase

import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.theme.Border

@NodeBuilderDslMarker
class ButtonBaseBuilder(val key: Any? = null) {
    companion object {
        fun buttonBase(key: Any? = null, block: ButtonBaseBuilder.() -> Unit) =
                ButtonBaseBuilder(key).apply(block).build()
    }

    var color: Color = Color(0.5f, 0.5f, 0.5f, 1.0f)
    var border: Border = Border()
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build() =
            Node(ButtonBaseComponent::class, ButtonBaseComponent.BaseProperties(color, border, nodes), key)
}