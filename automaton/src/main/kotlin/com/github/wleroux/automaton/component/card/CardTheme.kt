package com.github.wleroux.automaton.component.card

import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.theme.Padding

data class CardTheme(
        val color: Color = Color(1f, 1f, 1f, 1f),
        val border: Border = Border(),
        val margin: Padding = Padding(),
        val padding: Padding = Padding()
  )
