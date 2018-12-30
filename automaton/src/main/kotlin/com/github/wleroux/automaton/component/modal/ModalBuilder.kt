package com.github.wleroux.automaton.component.modal

import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class ModalBuilder(private val key: Any? = null) {
    companion object {
        fun modal(key: Any? = null, block: ModalBuilder.() -> Unit) =
                ModalBuilder(key).apply(block).build()
    }

    lateinit var theme: CardTheme
    var cancelModalHandler: () -> Unit = {}
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }
    fun build() =
            Node(ModalComponent::class, ModalComponent.ModalProperties(
                    theme,
                    cancelModalHandler,
                    nodes
            ), key)
}