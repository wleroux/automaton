package com.github.wleroux.automaton.component.startscreen

import com.github.wleroux.automaton.component.mainmenu.MainMenuBuilder.Companion.mainMenu
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node

fun startScreen(key: Any? = null)
    = Node(StartScreenComponent::class, Unit, key)

class StartScreenComponent: Component<String, Unit>("") {
    override fun asNodes() = listOf(mainMenu {
        quitHandler = {
            System.exit(1)
        }
        startGameHandler = {
            println("Start Game")
        }
        loadGameHandler = {
            println("Load Game")
        }
    })
}