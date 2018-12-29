package com.github.wleroux.keact.api.component.padding

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.theme.Padding

class PaddingBuilder(val key: Any? = null) {
    companion object {
        fun padding(key: Any? = null, block: PaddingBuilder.() -> Unit) =
                PaddingBuilder(key).apply(block).build()
    }

    var padding: Padding = Padding()
    private val nodes = mutableListOf<Node<*, *>>()

    operator fun Node<*,*>.unaryPlus() {
        nodes += this
    }

    fun build() = Node(PaddingComponent::class, PaddingComponent.PaddingProperties(
            padding, nodes
    ), key)
}