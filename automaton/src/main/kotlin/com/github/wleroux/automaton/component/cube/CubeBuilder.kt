package com.github.wleroux.automaton.component.cube

import com.github.wleroux.keact.api.Node

class CubeBuilder(val key: Any? = null) {
    companion object {
        fun cube(key: Any? = null, block: CubeBuilder.() -> Unit = {})
                = CubeBuilder(key).apply(block).build()
    }
    fun build() = Node(CubeComponent::class, Unit, key)
}