package com.github.wleroux.automaton.theme

import com.github.wleroux.keact.api.theme.Color

data class Theme(
        val defaultButtonTheme: ButtonTheme
)

val DEFAULT_THEME = Theme(
        defaultButtonTheme = ButtonTheme(
                defaultStyle = ButtonStyle(color = Color(0.5f, 0.5f, 0.5f, 1f)),
                disabledStyle = ButtonStyle(color = Color(0.1f, 0.1f, 0.1f, 1f)),
                hoveredStyle = ButtonStyle(color = Color(0.7f, 0.7f, 0.7f, 1f)),
                pressedStyle = ButtonStyle(color = Color(0.3f, 0.3f, 0.3f, 1f)),
                focusedStyle = ButtonStyle(color = Color(0.8f, 0.8f, 0.8f, 1f))
        )
)