package com.github.wleroux.keact.api.component.overlay

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class OverlayBuilder(val key: Any? = null) {
    companion object {
        fun overlay(key: Any? = null, block: OverlayBuilder.() -> Unit) =
                OverlayBuilder(key).apply(block).build()
    }

    private val nodes = mutableListOf<Node<*, *>>()

    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build() = Node(OverlayComponent::class, nodes, key)
}