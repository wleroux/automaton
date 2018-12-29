package com.github.wleroux.automaton.component.card

import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.theme.Padding

data class CardTheme(
        val color: Color = Color(0.5f, 0.5f, 0.5f, 1.0f),
        val border: Border = Border(),
        val margin: Padding = Padding(),
        val padding: Padding = Padding()
  )
