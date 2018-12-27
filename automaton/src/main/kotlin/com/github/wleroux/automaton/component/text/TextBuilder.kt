package com.github.wleroux.automaton.component.text

import com.github.wleroux.keact.api.Node

class TextBuilder(private val key: Any? = null) {
    companion object {
        @JvmStatic
        fun text(key: Any? = null, block: TextBuilder.() -> Unit): Node<Unit, String> {
            return TextBuilder(key).apply(block).build()
        }
    }

    lateinit var text: String
    operator fun String.unaryPlus() {
        text = this
    }

    fun build(): Node<Unit, String> {
        return Node(TextComponent::class, text, key)
    }
}