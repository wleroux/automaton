package com.github.wleroux.automaton.component.fpscounter

import com.github.wleroux.automaton.GameTick
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.theme.ThemeContext
import com.github.wleroux.bus.api.Bus
import com.github.wleroux.bus.api.BusSubscription
import com.github.wleroux.bus.api.message.MessageHandlerBuilder.Companion.messageHandler
import com.github.wleroux.bus.api.message.event.DefaultEventHandlerBuilder
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import java.util.concurrent.TimeUnit

class FPSCounterComponent: Component<FPSCounterComponent.Counter, Bus>(Counter()) {
    data class Counter(
            val fps: Int = 0,
            val ups: Int = 0
    )
    override fun asNodes() = listOf(
            ThemeContext.consumer { contextTheme ->
                +padding {
                    +text {
                        text = "FPS: ${state.fps} UPS: ${state.ups}"
                        theme = contextTheme.fpsTextTheme
                    }
                }
            })

    private var fpsTicks = 0
    private var upsTicks = 0
    private var lastUpdate = System.nanoTime()
    lateinit var subscription: BusSubscription
    override fun componentDidMount() {
        subscription = properties.subscribe(messageHandler {
            +DefaultEventHandlerBuilder.eventHandler<GameTick> {
                upsTicks++
            }
        })
    }

    override fun componentWillUnmount() {
        subscription.close()
    }

    override fun render() {
        val now = System.nanoTime()
        if (now - lastUpdate > TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS)) {
            state = state.copy(
                    fps = fpsTicks,
                    ups = upsTicks
            )
            upsTicks = 0
            fpsTicks = 0
            lastUpdate = now
        }
        fpsTicks++
        super.render()
    }
}