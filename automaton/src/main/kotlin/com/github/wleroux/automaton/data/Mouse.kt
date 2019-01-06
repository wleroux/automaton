package com.github.wleroux.automaton.data

import com.github.wleroux.automaton.component.window.event.*
import com.github.wleroux.keact.api.event.Event

class Mouse {
    private val pressed = mutableSetOf<MouseButton>()
    var xoffset: Double = 0.0
    var yoffset: Double = 0.0
    fun isPressed(value: MouseButton) = pressed.contains(value)
    fun handle(event: Event) {
        when (event.data) {
            is MouseScroll -> {
                val mouseScroll = event.data as MouseScroll
                xoffset = mouseScroll.xoffset
                yoffset = mouseScroll.yoffset
            }
            is MouseClick -> {
                val mouseClick = event.data as MouseClick
                if (mouseClick.action == MouseAction.PRESSED) {
                    pressed += mouseClick.button
                } else if (mouseClick.action == MouseAction.RELEASED) {
                    pressed -= mouseClick.button
                }
            }
        }
    }
}