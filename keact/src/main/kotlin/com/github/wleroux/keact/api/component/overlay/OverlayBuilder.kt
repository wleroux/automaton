package com.github.wleroux.keact.api.component.overlay

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.nodecollection.NodeCollectionBuilder

@NodeBuilderDslMarker
class OverlayBuilder(private val key: Any? = null): NodeCollectionBuilder() {
    companion object {
        fun overlay(key: Any? = null, block: OverlayBuilder.() -> Unit) =
                OverlayBuilder(key).apply(block).build()
    }

    fun build() = Node(key, OverlayComponent::class, nodes)
}