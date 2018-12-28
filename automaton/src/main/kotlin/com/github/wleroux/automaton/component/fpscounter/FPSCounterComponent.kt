package com.github.wleroux.automaton.component.fpscounter

import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.loadFont
import com.github.wleroux.automaton.math.Color4f
import com.github.wleroux.keact.api.Component
import java.util.concurrent.TimeUnit

class FPSCounterComponent: Component<Int, Unit>() {
    override var properties: Unit = Unit
    override var state: Int = 0
    var ticks = 0
    var lastUpdate = System.nanoTime()
    override fun asNodes() = listOf(
        text {
            text = "FPS: $state"
            size = 24
            font = loadFont("font/Roboto_Slab/Roboto_Slab-Regular.fnt")
            color = Color4f(1f, 1f, 1f, 1f)
        }
    )

    override fun render() {
        val now = System.nanoTime()
        if (now - lastUpdate > TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS)) {
            nextState = ticks
            ticks = 0
            lastUpdate = now
        }
        ticks ++
        super.render()
    }
}