package com.github.wleroux.automaton.component.modal

import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.nodecollection.NodeCollectionBuilder

@NodeBuilderDslMarker
class ModalBuilder(private val key: Any? = null): NodeCollectionBuilder() {
    companion object {
        fun modal(key: Any? = null, block: ModalBuilder.() -> Unit) =
                ModalBuilder(key).apply(block).build()
    }

    lateinit var theme: CardTheme
    var cancelModalHandler: () -> Unit = {}
    fun build() =
            Node(key, ModalComponent::class, ModalComponent.ModalProperties(
                    theme,
                    cancelModalHandler,
                    nodes
            ))
}