package com.github.wleroux.automaton.component.modal

import com.github.wleroux.automaton.component.card.CardBuilder.Companion.card
import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.automaton.component.window.event.*
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.dispatch
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase

class ModalComponent: Component<Unit, ModalComponent.ModalProperties>(Unit) {
    data class ModalProperties(
            val theme: CardTheme,
            val cancelModalHandler: () -> Unit,
            val nodes: List<Node<*, *>>
    )

    override fun componentDidMount() {
        dispatch(RequestFocus)
    }

    override fun handle(event: Event) {
        if (event.phase == Phase.CAPTURE) return

        when (event.data) {
            is MouseClick -> {
                val mouseClick = event.data as MouseClick
                if (mouseClick.action == MouseAction.RELEASED) {
                    properties.cancelModalHandler()
                }
                event.stopPropagation = true
            }
            is KeyStroke -> {
                val keyStroke = event.data as KeyStroke
                if (keyStroke.action == KeyAction.RELEASED && keyStroke.character == Keys.ESC) {
                    properties.cancelModalHandler()
                }
                event.stopPropagation = true
            }
        }
    }

    override fun asNodes() = listOf(card {
        theme = properties.theme
        +layout {
            properties.nodes.forEach { +it }
        }
    })
}