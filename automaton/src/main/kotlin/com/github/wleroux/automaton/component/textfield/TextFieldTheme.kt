package com.github.wleroux.automaton.component.textfield

data class TextFieldTheme(
        val defaultStyle: TextFieldStyle = TextFieldStyle(),
        val hoveredStyle: TextFieldStyle = TextFieldStyle(),
        val focusedStyle: TextFieldStyle = TextFieldStyle(),
        val disabledStyle: TextFieldStyle = TextFieldStyle()
)