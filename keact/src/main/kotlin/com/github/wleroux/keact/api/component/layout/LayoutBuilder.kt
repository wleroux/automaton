package com.github.wleroux.keact.api.component.layout

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class LayoutBuilder(private val key: Any?) {

    companion object {
        fun layout(key: Any? = null, block: LayoutBuilder.() -> Unit = {}) =
                LayoutBuilder(key).apply(block).build()
    }

    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    var direction: Direction = Direction.COLUMN_REVERSE
    var justifyContent: JustifyContent = JustifyContent.CENTER
    var alignContent: ContentAlign = ContentAlign.CENTER
    var alignItems: ItemAlign = ItemAlign.STRETCH
    var wrap: Wrap = Wrap.NO_WRAP

    fun build() = Node(LayoutComponent::class, LayoutComponent.LayoutProperties(direction, justifyContent, alignItems, alignContent, wrap, nodes), key)
}