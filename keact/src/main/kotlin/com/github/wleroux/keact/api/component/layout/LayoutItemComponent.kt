package com.github.wleroux.keact.api.component.layout

import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node

class LayoutItemComponent: Component<Unit, LayoutItemComponent.LayoutItemComponentProperties>(Unit) {
    interface LayoutItemProperties {
        val grow: Double
        val shrink: Double
        val alignSelf: ItemAlign?
    }

    data class LayoutItemComponentProperties(
            override val grow: Double = 0.0,
            override val shrink: Double = 1.0,
            override val alignSelf: ItemAlign? = null,
            val nodes: List<Node<*, *>>
    ): LayoutItemProperties

    override fun asNodes() = properties.nodes
}