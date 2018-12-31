package com.github.wleroux.automaton.component.launcher.fpscounter

import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.component.launcher.ThemeContext
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.component.layout.ContentAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import java.util.concurrent.TimeUnit

class FPSCounterComponent: Component<FPSCounterComponent.FpsCounterState, Unit>(FpsCounterState()) {
    data class FpsCounterState(
            val fps: Int = 0
    )
    override fun asNodes() = listOf(
            ThemeContext.consumer { contextTheme ->
                +layout {
                    justifyContent = JustifyContent.LEFT
                    alignContent = ContentAlign.END
                    +padding {
                        +text {
                            text = "FPS: ${state.fps}"
                            theme = contextTheme.fpsTextTheme
                        }
                    }
                }
            })

    private var fpsTicks = 0
    private var upsTicks = 0
    private var lastUpdate = System.nanoTime()
    override fun render() {
        val now = System.nanoTime()
        if (now - lastUpdate > TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS)) {
            state = state.copy(
                    fps = fpsTicks
            )
            upsTicks = 0
            fpsTicks = 0
            lastUpdate = now
        }
        fpsTicks++
        super.render()
    }

    override fun findComponentAt(x: Int, y: Int): Component<*, *>? = null
}