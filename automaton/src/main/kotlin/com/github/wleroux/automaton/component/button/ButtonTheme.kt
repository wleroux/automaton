package com.github.wleroux.automaton.component.button

data class ButtonTheme(
        val defaultStyle: ButtonStyle,
        val disabledStyle: ButtonStyle,
        val hoveredStyle: ButtonStyle,
        val pressedStyle: ButtonStyle,
        val focusedStyle: ButtonStyle
)