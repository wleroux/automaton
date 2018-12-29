package com.github.wleroux.automaton.component.card

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class CardBuilder(val key: Any? = null) {
    companion object {
        fun card(key: Any? = null, block: CardBuilder.() -> Unit) =
                CardBuilder(key).apply(block).build()
    }

    var theme: CardTheme = CardTheme()
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    fun build() =
            Node(CardComponent::class, CardComponent.CardProperties(theme, nodes), key)
}