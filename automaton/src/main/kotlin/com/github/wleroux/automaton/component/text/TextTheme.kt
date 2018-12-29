package com.github.wleroux.automaton.component.text

import com.github.wleroux.automaton.component.text.font.Font
import com.github.wleroux.automaton.loadFont
import com.github.wleroux.keact.api.theme.Color

data class TextTheme(
    val size: Int = 20,
    val color: Color = Color.WHITE,
    val font: Font = loadFont("font/Roboto_Slab/Roboto_Slab-Regular.fnt")
)
