package com.github.wleroux.automaton.component.launcher.game.viewport

import com.github.wleroux.automaton.component.window.event.KeyAction
import com.github.wleroux.automaton.component.window.event.KeyStroke
import com.github.wleroux.keact.api.event.Event

class Keyboard {
    private val pressed = mutableSetOf<Char>()

    fun isPressed(value: Char) = pressed.contains(value)
    fun handle(event: Event) {
        when (event.data) {
            is KeyStroke -> {
                val keyStroke = event.data as KeyStroke
                if (keyStroke.action == KeyAction.PRESSED) {
                    println("Key Pressed: ${keyStroke.character.toInt()}")
                    pressed += keyStroke.character
                } else if (keyStroke.action == KeyAction.RELEASED) {
                    pressed -= keyStroke.character
                    println("Key Released: ${keyStroke.character.toInt()}")
                }
            }
        }
    }
}