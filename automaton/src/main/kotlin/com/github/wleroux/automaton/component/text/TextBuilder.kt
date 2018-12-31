package com.github.wleroux.automaton.component.text

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class TextBuilder(val key: Any? = null) {
    companion object {
        fun text(key: Any? = null, block: TextBuilder.() -> Unit) =
                TextBuilder(key).apply(block).build()
    }

    lateinit var text: String
    lateinit var theme: TextTheme

    fun build() = Node(key, TextComponent::class, TextComponent.TextProperties(
            text,
            theme
    ))
}