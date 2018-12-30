package com.github.wleroux.automaton.component.surface

import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class SurfaceBuilder(private val key: Any? = null) {
    companion object {
        fun surface(key: Any? = null, block: SurfaceBuilder.() -> Unit) =
                SurfaceBuilder(key).apply(block).build()
    }

    lateinit var theme: CardTheme
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build() =
            Node(SurfaceComponent::class, SurfaceComponent.SurfaceProperties(
                    theme,
                    nodes
            ), key)
}