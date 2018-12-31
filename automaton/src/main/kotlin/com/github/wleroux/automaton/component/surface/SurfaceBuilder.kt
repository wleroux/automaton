package com.github.wleroux.automaton.component.surface

import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.nodecollection.NodeCollectionBuilder

@NodeBuilderDslMarker
class SurfaceBuilder(private val key: Any? = null): NodeCollectionBuilder() {
    companion object {
        fun surface(key: Any? = null, block: SurfaceBuilder.() -> Unit) =
                SurfaceBuilder(key).apply(block).build()
    }

    lateinit var theme: CardTheme
    fun build() =
            Node(key, SurfaceComponent::class, SurfaceComponent.SurfaceProperties(
                    theme,
                    nodes
            ))
}