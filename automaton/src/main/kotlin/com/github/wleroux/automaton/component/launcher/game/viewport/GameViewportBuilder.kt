package com.github.wleroux.automaton.component.launcher.game.viewport

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class GameViewportBuilder(val key: Any? = null) {
    companion object {
        fun gameViewport(key: Any? = null, block: GameViewportBuilder.() -> Unit = {})
                = GameViewportBuilder(key).apply(block).build()
    }
    fun build() = Node(GameViewportComponent::class, Unit, key)
}