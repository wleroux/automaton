package com.github.wleroux.automaton.component.text

import com.github.wleroux.automaton.component.text.font.Font
import com.github.wleroux.automaton.loadFont
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.Node

class TextBuilder(val key: Any? = null) {
    companion object {
        fun text(key: Any? = null, block: TextBuilder.() -> Unit) =
                TextBuilder(key).apply(block).build()
    }

    lateinit var text: String
    var size: Int = 24
    var color: Color = Color.WHITE
    var font: Font = loadFont("font/Roboto_Slab/Roboto_Slab-Regular.fnt")

    fun build() = Node(TextComponent::class, TextComponent.TextProperties(
            text,
            size,
            color,
            font
    ), key)
}