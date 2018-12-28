package com.github.wleroux.keact.api.component.padding

import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node

class PaddingComponent: Component<Unit, PaddingComponent.PaddingProperties>() {
    data class PaddingProperties(
            val top: Int,
            val right: Int,
            val bottom: Int,
            val left: Int,
            val nodes: List<Node<*, *>>
    )

    override lateinit var properties: PaddingProperties
    override var state: Unit = Unit

    override fun asNodes() = properties.nodes

    override fun preferredWidth(parentWidth: Int, parentHeight: Int): Int =
            childComponents.values.map {
                properties.left + properties.right + it.preferredWidth(parentWidth, parentHeight)
            }.max() ?: properties.left + properties.right
    override fun preferredHeight(parentWidth: Int, parentHeight: Int): Int =
            childComponents.values.map {
                properties.top + properties.bottom + it.preferredHeight(parentWidth, parentHeight)
            }.max() ?: properties.top + properties.bottom
    override fun render(): Unit = childComponents.values.forEach { childComponent ->
        childComponent.x = x + properties.left
        childComponent.y = y + properties.bottom
        childComponent.width = width - properties.left - properties.right
        childComponent.height = height - properties.top - properties.bottom
        childComponent.render()
    }
}