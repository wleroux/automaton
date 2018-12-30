package com.github.wleroux.automaton.theme

import com.github.wleroux.automaton.component.button.ButtonStyle
import com.github.wleroux.automaton.component.button.ButtonTheme
import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.automaton.component.text.TextTheme
import com.github.wleroux.automaton.component.textfield.TextFieldStyle
import com.github.wleroux.automaton.component.textfield.TextFieldTheme
import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.theme.Padding

data class Theme(
        val fpsTextTheme: TextTheme,
        val modalCardTheme: CardTheme,
        val primaryTextFieldTheme: TextFieldTheme,
        val secondaryTextFieldTheme: TextFieldTheme,
        val primaryCardTheme: CardTheme,
        val primaryCardTextTheme: TextTheme,
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
                                radius = Border.BorderRadius(4, 4, 4, 4)
                        ),
                        padding = Padding(4, 8, 4, 8),
                        margin = Padding(4, 4, 4,4)
                )
        )
        val defaultTextFieldStyle = TextFieldStyle(
                textTheme = defaultTextTheme,
                cardTheme = CardTheme(
                        border = Border(
                                width = Border.BorderWidth(0, 0, 2, 0),
                                radius = Border.BorderRadius(8, 8, 0, 0)
                        ),
                        padding = Padding(4, 8, 4, 8),
                        margin = Padding(4, 4, 4, 4)
                )
        )
        val defaultTextFieldTheme = TextFieldTheme(
                defaultStyle = defaultTextFieldStyle.copy(
                        textTheme = defaultTextFieldStyle.textTheme.copy(
                                color = Color.BLUEGREY_800
                        ),
                        placeholderTextTheme = defaultTextFieldStyle.textTheme.copy(
                                color = Color.BLUEGREY_300
                        ),
                        cardTheme = defaultTextFieldStyle.cardTheme.copy(
                                color = Color.BLUEGREY_50,
                                border = defaultTextFieldStyle.cardTheme.border.copy(
                                        color = Color.BLUEGREY_600
                                )
                        )
                ),
                hoveredStyle = defaultTextFieldStyle.copy(
                        textTheme = defaultTextFieldStyle.textTheme.copy(
                                color = Color.BLUEGREY_800
                        ),
                        placeholderTextTheme = defaultTextFieldStyle.textTheme.copy(
                                color = Color.BLUEGREY_300
                        ),
                        cardTheme = defaultTextFieldStyle.cardTheme.copy(
                                color = Color.BLUEGREY_50,
                                border = defaultTextFieldStyle.cardTheme.border.copy(
                                        color = Color.BLUEGREY_900
                                )
                        )
                ),
                focusedStyle = defaultTextFieldStyle.copy(
                        textTheme = defaultTextFieldStyle.textTheme.copy(
                                color = Color.BLUEGREY_900
                        ),
                        placeholderTextTheme = defaultTextFieldStyle.textTheme.copy(
                                color = Color.BLUEGREY_400
                        ),
                        cardTheme = defaultTextFieldStyle.cardTheme.copy(
                                color = Color.BLUEGREY_100,
                                border = defaultTextFieldStyle.cardTheme.border.copy(
                                        color = Color.BLUEGREY_900
                                )
                        )
                ),
                disabledStyle = defaultTextFieldStyle.copy(
                        textTheme = defaultTextFieldStyle.textTheme.copy(
                                color = Color.BLUEGREY_300
                        ),
                        placeholderTextTheme = defaultTextFieldStyle.textTheme.copy(
                                color = Color.BLUEGREY_200
                        ),
                        cardTheme = defaultTextFieldStyle.cardTheme.copy(
                                color = Color.BLUEGREY_50,
                                border = defaultTextFieldStyle.cardTheme.border.copy(
                                        color = Color.BLUEGREY_100
                                )
                        )
                )
        )

        Theme(
                fpsTextTheme = defaultTextTheme.copy(
                        size = 15
                ),
                modalCardTheme = CardTheme(
                        color = Color(0f, 0f, 0f, 0.5f)
                ),
                primaryTextFieldTheme = defaultTextFieldTheme,
                secondaryTextFieldTheme = defaultTextFieldTheme.copy(
                        defaultStyle = defaultTextFieldStyle.copy(
                                textTheme = defaultTextFieldStyle.textTheme.copy(
                                        color = Color.ORANGE_800
                                ),
                                placeholderTextTheme = defaultTextFieldStyle.textTheme.copy(
                                        color = Color.ORANGE_300
                                ),
                                cardTheme = defaultTextFieldStyle.cardTheme.copy(
                                        color = Color.ORANGE_50,
                                        border = defaultTextFieldTheme.defaultStyle.cardTheme.border.copy(
                                                color = Color.ORANGE_600
                                        )
                                )
                        ),
                        hoveredStyle = defaultTextFieldStyle.copy(
                                textTheme = defaultTextFieldStyle.textTheme.copy(
                                        color = Color.ORANGE_800
                                ),
                                placeholderTextTheme = defaultTextFieldStyle.textTheme.copy(
                                        color = Color.ORANGE_300
                                ),
                                cardTheme = defaultTextFieldStyle.cardTheme.copy(
                                        color = Color.ORANGE_50,
                                        border = defaultTextFieldTheme.defaultStyle.cardTheme.border.copy(
                                                color = Color.ORANGE_900
                                        )
                                )
                        ),
                        focusedStyle = defaultTextFieldStyle.copy(
                                textTheme = defaultTextFieldStyle.textTheme.copy(
                                        color = Color.ORANGE_900
                                ),
                                placeholderTextTheme = defaultTextFieldStyle.textTheme.copy(
                                        color = Color.ORANGE_400
                                ),
                                cardTheme = defaultTextFieldStyle.cardTheme.copy(
                                        color = Color.ORANGE_100,
                                        border = defaultTextFieldTheme.defaultStyle.cardTheme.border.copy(
                                                color = Color.ORANGE_900
                                        )
                                )
                        ),
                        disabledStyle = defaultTextFieldStyle.copy(
                                textTheme = defaultTextFieldStyle.textTheme.copy(
                                        color = Color.ORANGE_300
                                ),
                                placeholderTextTheme = defaultTextFieldStyle.textTheme.copy(
                                        color = Color.ORANGE_200
                                ),
                                cardTheme = defaultTextFieldStyle.cardTheme.copy(
                                        color = Color.ORANGE_50,
                                        border = defaultTextFieldTheme.defaultStyle.cardTheme.border.copy(
                                                color = Color.ORANGE_100
                                        )
                                )
                        )
                ),
                primaryCardTheme = CardTheme(
                        color = Color.WHITE,
                        border = Border(
                                radius = Border.BorderRadius(1, 1, 1, 1)
                        ),
                        padding = Padding( 8, 8, 8, 8),
                        margin = Padding(4, 4, 4, 4)
                ),
                primaryCardTextTheme = defaultTextTheme.copy(
                        color = Color.BLACK
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
