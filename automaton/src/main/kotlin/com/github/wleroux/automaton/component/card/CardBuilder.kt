package com.github.wleroux.automaton.component.card

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.nodecollection.NodeCollectionBuilder

@NodeBuilderDslMarker
class CardBuilder(private val key: Any? = null): NodeCollectionBuilder() {
    companion object {
        fun card(key: Any? = null, block: CardBuilder.() -> Unit) =
                CardBuilder(key).apply(block).build()
    }

    var theme: CardTheme = CardTheme()
    fun build() =
            Node(key, CardComponent::class, CardComponent.CardProperties(theme, nodes))
}