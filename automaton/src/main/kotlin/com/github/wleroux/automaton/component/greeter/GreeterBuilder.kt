package com.github.wleroux.automaton.component.greeter

import com.github.wleroux.keact.api.Node

class GreeterBuilder(private val key: Any? = null) {
    companion object {
        @JvmStatic
        fun build(key: Any? = null, block: GreeterBuilder.() -> Unit): Node<Unit, List<Node<*, *>>> {
            return GreeterBuilder(key).apply(block).build()
        }
    }

    private val nodes: MutableList<Node<*, *>> = mutableListOf()
    operator fun Node<*,*>.unaryPlus() {
        nodes += this
    }
    fun build(): Node<Unit, List<Node<*, *>>> {
        return Node(GreeterComponent::class, nodes, key)
    }
}