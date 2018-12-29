package com.github.wleroux.automaton.theme

import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.theme.Padding

data class ButtonStyle(
        val color: Color = Color(0.5f, 0.5f, 0.5f, 1f),
        val border: Border = Border(),
        val padding: Padding = Padding(),
        val margin: Padding = Padding()
)
