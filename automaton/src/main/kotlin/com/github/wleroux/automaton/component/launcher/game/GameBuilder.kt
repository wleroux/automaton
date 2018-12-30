package com.github.wleroux.automaton.component.launcher.game

import com.github.wleroux.keact.api.Node

class GameBuilder(private val key: Any?) {
    companion object {
        fun game(key: Any? = null, block: GameBuilder.() -> Unit) =
                GameBuilder(key).apply(block).build()
    }

    lateinit var quitToMainMenuHandler: () -> Unit

    fun build() =
            Node(GameComponent::class, GameComponent.GameProperties(
                    quitToMainMenuHandler
            ), key)
}