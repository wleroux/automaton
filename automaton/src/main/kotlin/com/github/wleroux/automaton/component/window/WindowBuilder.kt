package com.github.wleroux.automaton.component.window

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class WindowBuilder(private val key: Any? = null) {
    companion object {
        @JvmStatic
        fun window(key: Any? = null, block: WindowBuilder.() -> Unit = {}) = WindowBuilder(key).apply(block).build()
    }

    private val nodes: MutableList<Node<*, *>> = mutableListOf()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build(): Node<Unit, List<Node<*, *>>> {
        return Node(WindowComponent::class, nodes, key)
    }
}