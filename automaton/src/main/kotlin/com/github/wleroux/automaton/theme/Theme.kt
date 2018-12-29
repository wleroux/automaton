package com.github.wleroux.automaton.theme

import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.theme.Padding

data class Theme(
        val primaryButtonTheme: ButtonTheme,
        val secondaryButtonTheme: ButtonTheme
)


val DEFAULT_THEME by lazy {
        val primaryDefaultButtonStyle = ButtonStyle(color = Color.BLUEGREY_800,
                border = Border(
                        width = Border.BorderWidth(0, 0, 0, 0),
                        radius = Border.BorderRadius(2, 2, 2, 2)
                ),
                padding = Padding(8, 16, 8, 16),
                margin = Padding(4, 4, 4, 4)
        )

        Theme(
                primaryButtonTheme = ButtonTheme(
                        defaultStyle = primaryDefaultButtonStyle,
                        disabledStyle = primaryDefaultButtonStyle.copy(color = Color.BLUEGREY_900),
                        hoveredStyle = primaryDefaultButtonStyle.copy(color = Color.BLUEGREY_700),
                        pressedStyle = primaryDefaultButtonStyle.copy(color = Color.BLUEGREY_900),
                        focusedStyle = primaryDefaultButtonStyle.copy(color = Color.BLUEGREY_600)
                ),
                secondaryButtonTheme = ButtonTheme(
                        defaultStyle = primaryDefaultButtonStyle.copy(color = Color.ORANGE_800),
                        disabledStyle = primaryDefaultButtonStyle.copy(color = Color.ORANGE_900),
                        hoveredStyle = primaryDefaultButtonStyle.copy(color = Color.ORANGE_700),
                        pressedStyle = primaryDefaultButtonStyle.copy(color = Color.ORANGE_900),
                        focusedStyle = primaryDefaultButtonStyle.copy(color = Color.ORANGE_600)
                )
        )
}
