package com.github.wleroux.keact.api.component.padding

import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.theme.Padding

class PaddingComponent: Component<Unit, PaddingComponent.PaddingProperties>(Unit) {
    data class PaddingProperties(
            val padding: Padding,
            val nodes: List<Node<*, *>>
    )

    override fun asNodes() = properties.nodes

    override fun preferredWidth(parentWidth: Int, parentHeight: Int): Int =
            childComponents.values.map {
                properties.padding.left + properties.padding.right + it.preferredWidth(parentWidth, parentHeight)
            }.max() ?: properties.padding.left + properties.padding.right
    override fun preferredHeight(parentWidth: Int, parentHeight: Int): Int =
            childComponents.values.map {
                properties.padding.top + properties.padding.bottom + it.preferredHeight(parentWidth, parentHeight)
            }.max() ?: properties.padding.top + properties.padding.bottom
    override fun render(): Unit = childComponents.values.forEach { childComponent ->
        childComponent.x = x + properties.padding.left
        childComponent.y = y + properties.padding.bottom
        childComponent.width = width - properties.padding.left - properties.padding.right
        childComponent.height = height - properties.padding.top - properties.padding.bottom
        childComponent.render()
    }
}