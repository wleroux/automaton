package com.github.wleroux.automaton.component.window.event

data class MouseClick(
    val action: MouseAction,
    val button: MouseButton,
    val mouseX: Int,
    val mouseY: Int,
    val shiftDown: Boolean,
    val ctrlDown: Boolean,
    val altDown: Boolean,
    val metaDown: Boolean
)

