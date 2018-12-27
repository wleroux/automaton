package com.github.wleroux.automaton

import com.github.wleroux.automaton.component.greeter.GreeterBuilder.Companion.greeter
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.keact.api.*

fun main(args: Array<String>) {

    val greeterComponent = greeter { +text { +"World" } }.mount()
    greeterComponent.render()

    var counter: Int = 0
    while (counter < 7) {
        val greeterNode = greeter("greeter") {
            +text { +"World" }
            +text { +"[" }
            +text { +counter.toString() }
            +text { +"]" }
        }

        greeterComponent.update(greeterNode.properties)
        greeterComponent.render()
        counter ++
    }

    greeterComponent.dispatch(Click)
    greeterComponent.unmount()
}
