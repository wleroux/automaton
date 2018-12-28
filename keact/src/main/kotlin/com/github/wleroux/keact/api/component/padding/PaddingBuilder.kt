package com.github.wleroux.keact.api.component.padding

import com.github.wleroux.keact.api.Node

class PaddingBuilder(val key: Any? = null) {
    companion object {
        fun padding(key: Any? = null, block: PaddingBuilder.() -> Unit) =
                PaddingBuilder(key).apply(block).build()
    }

    var top: Int = 0
    var right: Int = 0
    var bottom: Int = 0
    var left: Int = 0

    private val nodes = mutableListOf<Node<*, *>>()

    operator fun Node<*,*>.unaryPlus() {
        nodes += this
    }

    fun build() = Node(PaddingComponent::class, PaddingComponent.PaddingProperties(
            top, right, bottom, left, nodes
    ), key)
}