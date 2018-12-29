package com.github.wleroux.automaton.component.textfield

import com.github.wleroux.keact.api.Node

class TextFieldBuilder(private val key: Any? = null) {
    companion object {
        fun textField(key: Any? = null, block: TextFieldBuilder.() -> Unit) =
                TextFieldBuilder(key).apply(block).build()
    }

    var theme: TextFieldTheme = TextFieldTheme()
    var textChangeHandler: (String) -> Boolean = {true}
    var placeholderText: String = ""
    var initialText: String = ""
    var disabled: Boolean = false

    fun build() =
            Node(TextFieldComponent::class, TextFieldComponent.TextFieldProperties(theme, initialText, placeholderText, textChangeHandler, disabled), key)
}