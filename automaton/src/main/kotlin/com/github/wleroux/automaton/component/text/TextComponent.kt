package com.github.wleroux.automaton.component.text

import com.github.wleroux.keact.api.Component

class TextComponent : Component<Unit, String>() {
    override var properties: String = "Hello, World!"
    override var state: Unit = Unit
    override fun render() {
        print(properties)
    }
}
