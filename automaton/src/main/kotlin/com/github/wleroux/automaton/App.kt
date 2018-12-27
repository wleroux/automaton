package com.github.wleroux.automaton

import com.github.wleroux.automaton.component.Components.greeter
import com.github.wleroux.automaton.component.Components.text
import com.github.wleroux.automaton.component.greeter.Click
import com.github.wleroux.keact.api.ComponentManager

fun main(args: Array<String>) {
    val greeterNode = greeter("greeter") {
        +text("name") { +"World"}
    }

    val greeterComponent = ComponentManager.mount(greeterNode.type, greeterNode.properties)
    greeterComponent.render()

    ComponentManager.dispatch(Click(greeterComponent))
}
