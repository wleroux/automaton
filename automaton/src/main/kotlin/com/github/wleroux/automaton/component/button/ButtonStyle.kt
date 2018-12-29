package com.github.wleroux.automaton.component.button

import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.automaton.component.text.TextTheme
import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.theme.Padding

data class ButtonStyle(
        val textTheme: TextTheme = TextTheme(),
        val cardTheme: CardTheme = CardTheme()
)
