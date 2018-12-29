package com.github.wleroux.keact.api.component.layout

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class LayoutItemBuilder(private val key: Any?) {

    companion object {
        fun layoutItem(key: Any? = null, block: LayoutItemBuilder.() -> Unit = {}) =
                LayoutItemBuilder(key).apply(block).build()
    }

    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    var grow: Double = 0.0
    var shrink: Double = 1.0
    var alignSelf: ItemAlign? = null

    fun build() = Node(LayoutItemComponent::class, LayoutItemComponent.LayoutItemComponentProperties(grow, shrink, alignSelf, nodes), key)
}