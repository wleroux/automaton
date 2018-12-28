package com.github.wleroux.keact.api.component.layout

import com.github.wleroux.keact.api.Node

class LayoutBuilder(private val key: Any?) {

    companion object {
        fun layout(key: Any? = null, block: LayoutBuilder.() -> Unit = {}) =
                LayoutBuilder(key).apply(block).build()
    }

    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    var direction: Direction = Direction.ROW
    var justifyContent: JustifyContent = JustifyContent.LEFT
    var alignItems: ItemAlign = ItemAlign.START
    var alignContent: ContentAlign = ContentAlign.START
    var wrap: Wrap = Wrap.NO_WRAP

    fun build() = Node(LayoutComponent::class, LayoutComponent.LayoutProperties(direction, justifyContent, alignItems, alignContent, wrap, nodes), key)
}