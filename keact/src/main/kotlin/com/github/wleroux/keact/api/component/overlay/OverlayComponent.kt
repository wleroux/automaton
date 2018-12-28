package com.github.wleroux.keact.api.component.overlay

import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node

class OverlayComponent: Component<Unit, List<Node<*, *>>>() {
    override lateinit var properties: List<Node<*, *>>
    override var state: Unit = Unit

    override fun asNodes(): List<Node<*, *>> = properties
}