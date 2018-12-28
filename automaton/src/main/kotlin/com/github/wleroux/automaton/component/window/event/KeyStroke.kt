package com.github.wleroux.automaton.component.window.event


data class KeyStroke(
        val character: Char,
        val action: KeyAction,
        val shiftDown: Boolean,
        val ctrlDown: Boolean,
        val altDown: Boolean,
        val metaDown: Boolean
)