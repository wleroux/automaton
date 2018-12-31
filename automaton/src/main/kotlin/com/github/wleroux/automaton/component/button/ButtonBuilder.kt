package com.github.wleroux.automaton.component.button

import com.github.wleroux.automaton.component.launcher.DEFAULT_THEME
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.nodecollection.NodeCollectionBuilder
import com.github.wleroux.keact.api.event.Event

@NodeBuilderDslMarker
class ButtonBuilder(private val key: Any? = null): NodeCollectionBuilder() {
    companion object {
        fun button(key: Any? = null, block: ButtonBuilder.() -> Unit = {}) =
                ButtonBuilder(key).apply(block).build()
    }

    var disabled: Boolean = false
    var theme: ButtonTheme = DEFAULT_THEME.primaryButtonTheme
    var clickHandler: (Event) -> Unit = {}
    fun build() =
            Node(key, ButtonComponent::class, ButtonComponent.ButtonProperties(
                    disabled, theme, clickHandler, nodes
            ))
}