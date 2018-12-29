package com.github.wleroux.automaton.component.textfield

import com.github.wleroux.automaton.component.card.CardTheme
import com.github.wleroux.automaton.component.text.TextTheme

data class TextFieldStyle(
        val textTheme: TextTheme = TextTheme(),
        val placeholderTextTheme: TextTheme = TextTheme(),
        val cardTheme: CardTheme = CardTheme()
)