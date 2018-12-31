package com.github.wleroux.automaton.component.button

import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.automaton.component.text.TextTheme

data class ButtonStyle(
        val textTheme: TextTheme,
        val cardTheme: CardTheme = CardTheme()
)
