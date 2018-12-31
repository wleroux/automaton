package com.github.wleroux.automaton.component.launcher.game.viewport

import com.github.wleroux.ecs.api.Game
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class GameViewportBuilder(val key: Any? = null) {
    companion object {
        fun gameViewport(key: Any? = null, block: GameViewportBuilder.() -> Unit = {})
                = GameViewportBuilder(key).apply(block).build()
    }

    lateinit var game: Game

    fun build() = Node(key, GameViewportComponent::class, game)
}