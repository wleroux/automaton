package com.github.wleroux.automaton.component.text

import com.github.wleroux.automaton.component.text.font.Font
import com.github.wleroux.automaton.math.Color4f
import com.github.wleroux.keact.api.Node

class TextBuilder(val key: Any? = null) {
    companion object {
        fun text(key: Any? = null, block: TextBuilder.() -> Unit) =
                TextBuilder(key).apply(block).build()
    }

    var size: Int = 0
    lateinit var text: String
    lateinit var color: Color4f
    lateinit var font: Font

    fun build() = Node(TextComponent::class, TextComponent.TextProperties(
            text,
            size,
            color,
            font
    ), key)
}