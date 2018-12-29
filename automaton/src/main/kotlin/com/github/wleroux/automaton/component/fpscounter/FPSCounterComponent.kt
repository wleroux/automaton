package com.github.wleroux.automaton.component.fpscounter

import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.theme.ThemeContext
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import java.util.concurrent.TimeUnit

class FPSCounterComponent: Component<Int, Unit>(0) {
    var ticks = 0
    var lastUpdate = System.nanoTime()
    override fun asNodes() = listOf(
            ThemeContext.consumer { contextTheme ->
                +padding {
                    +text {
                        text = "FPS: $state"
                        theme = contextTheme.fpsTextTheme
                    }
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