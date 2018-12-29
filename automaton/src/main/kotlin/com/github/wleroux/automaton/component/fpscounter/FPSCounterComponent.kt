package com.github.wleroux.automaton.component.fpscounter

import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.loadFont
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import com.github.wleroux.keact.api.theme.Padding
import java.util.concurrent.TimeUnit

class FPSCounterComponent: Component<Int, Unit>(0) {
    var ticks = 0
    var lastUpdate = System.nanoTime()
    override fun asNodes() = listOf(
        padding {
            +text {
                text = "FPS: $state"
                size = 24
                font = loadFont("font/Roboto_Slab/Roboto_Slab-Regular.fnt")
                color = Color.WHITE
            }
        })

    override fun render() {
        val now = System.nanoTime()
        if (now - lastUpdate > TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS)) {
            state = ticks
            ticks = 0
            lastUpdate = now
        }
        ticks++
        super.render()
    }
}