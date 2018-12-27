package com.github.wleroux.automaton.component

import com.github.wleroux.automaton.component.greeter.GreeterBuilder
import com.github.wleroux.automaton.component.text.TextBuilder

object Components {
    fun text(key: Any? = null, block: TextBuilder.() -> Unit) = TextBuilder.build(key, block)
    fun greeter(key: Any? = null, block: GreeterBuilder.() -> Unit) = GreeterBuilder.build(key, block)
}