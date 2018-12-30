package com.github.wleroux.automaton.component.surface

import com.github.wleroux.automaton.component.card.CardBuilder.Companion.card
import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase

class SurfaceComponent: Component<Unit, SurfaceComponent.SurfaceProperties>(Unit) {
    data class SurfaceProperties(
            val theme: CardTheme,
            val nodes: List<Node<*, *>>
    )

    override fun handle(event: Event) {
        if (event.phase == Phase.CAPTURE) return
        event.stopPropagation = true
    }

    override fun asNodes() = listOf(card {
        theme = properties.theme
        properties.nodes.forEach { +it }
    })
}