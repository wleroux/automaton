package com.github.wleroux.keact.api.component.layout

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.nodecollection.NodeCollectionBuilder

@NodeBuilderDslMarker
class LayoutItemBuilder(private val key: Any? = null): NodeCollectionBuilder() {
    companion object {
        fun layoutItem(key: Any? = null, block: LayoutItemBuilder.() -> Unit = {}) =
                LayoutItemBuilder(key).apply(block).build()
    }

    var grow: Double = 0.0
    var shrink: Double = 1.0
    var alignSelf: ItemAlign? = null
    fun build() =
            Node(key, LayoutItemComponent::class, LayoutItemComponent.LayoutItemComponentProperties(grow, shrink, alignSelf, nodes))
}