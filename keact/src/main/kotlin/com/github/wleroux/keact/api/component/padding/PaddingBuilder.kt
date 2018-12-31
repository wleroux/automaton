package com.github.wleroux.keact.api.component.padding

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.nodecollection.NodeCollectionBuilder
import com.github.wleroux.keact.api.theme.Padding

@NodeBuilderDslMarker
class PaddingBuilder(val key: Any? = null): NodeCollectionBuilder() {
    companion object {
        fun padding(key: Any? = null, block: PaddingBuilder.() -> Unit) =
                PaddingBuilder(key).apply(block).build()
    }

    var padding: Padding = Padding()

    fun build() = Node(key, PaddingComponent::class, PaddingComponent.PaddingProperties(
            padding, nodes
    ))
}