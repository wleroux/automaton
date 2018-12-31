package com.github.wleroux.automaton.component.window

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.nodecollection.NodeCollectionBuilder

@NodeBuilderDslMarker
class WindowBuilder(private val key: Any? = null): NodeCollectionBuilder() {
    companion object {
        @JvmStatic
        fun window(key: Any? = null, block: WindowBuilder.() -> Unit = {}) =
                WindowBuilder(key).apply(block).build()
    }

    fun build() = Node(key, WindowComponent::class, nodes)
}