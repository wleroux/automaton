package com.github.wleroux.automaton.theme

import com.github.wleroux.automaton.component.button.ButtonStyle
import com.github.wleroux.automaton.component.button.ButtonTheme
import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.automaton.component.text.TextTheme
import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.theme.Padding

data class Theme(
        val fpsTextTheme: TextTheme,
        val primaryCardTheme: CardTheme,
        val primaryButtonTheme: ButtonTheme,
        val secondaryButtonTheme: ButtonTheme
)

val DEFAULT_THEME by lazy {
        val defaultTextTheme = TextTheme()
        val defaultButtonStyle = ButtonStyle(
                textTheme = defaultTextTheme,
                cardTheme = CardTheme(
                        color = Color.BLUEGREY_800,
                        border = Border(
                                width = Border.BorderWidth(0, 0, 0, 0),
                                radius = Border.BorderRadius(2, 2, 2, 2)
                        ),
                        padding = Padding(4, 8, 4, 8),
                        margin = Padding(4, 4, 4,4)
                )
        )

        Theme(
                fpsTextTheme = defaultTextTheme,
                primaryCardTheme = CardTheme(
                        color = Color(1f, 1f, 1f, 1f),
                        border = Border(
                                radius = Border.BorderRadius(2, 2, 2, 2)
                        ),
                        padding = Padding( 4, 4, 4, 4),
                        margin = Padding(4, 4, 4, 4)
                ),
                primaryButtonTheme = ButtonTheme(
                        defaultStyle = defaultButtonStyle,
                        disabledStyle = defaultButtonStyle.copy(cardTheme = defaultButtonStyle.cardTheme.copy(
                                color = Color.BLUEGREY_900)),
                        hoveredStyle = defaultButtonStyle.copy(cardTheme = defaultButtonStyle.cardTheme.copy(
                                color = Color.BLUEGREY_700)),
                        pressedStyle = defaultButtonStyle.copy(cardTheme = defaultButtonStyle.cardTheme.copy(
                                color = Color.BLUEGREY_900)),
                        focusedStyle = defaultButtonStyle.copy(cardTheme = defaultButtonStyle.cardTheme.copy(
                                color = Color.BLUEGREY_600))
                ),
                secondaryButtonTheme = ButtonTheme(
                        defaultStyle = defaultButtonStyle.copy(cardTheme = defaultButtonStyle.cardTheme.copy(
                                color = Color.ORANGE_800)),
                        disabledStyle = defaultButtonStyle.copy(cardTheme = defaultButtonStyle.cardTheme.copy(
                                color = Color.ORANGE_900)),
                        hoveredStyle = defaultButtonStyle.copy(cardTheme = defaultButtonStyle.cardTheme.copy(
                                color = Color.ORANGE_700)),
                        pressedStyle = defaultButtonStyle.copy(cardTheme = defaultButtonStyle.cardTheme.copy(
                                color = Color.ORANGE_900)),
                        focusedStyle = defaultButtonStyle.copy(cardTheme = defaultButtonStyle.cardTheme.copy(
                                color = Color.ORANGE_600))
                )
        )
}
