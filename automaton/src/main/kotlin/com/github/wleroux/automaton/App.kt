package com.github.wleroux.automaton

import com.github.wleroux.automaton.bus.BusContext
import com.github.wleroux.automaton.component.cube.CubeBuilder.Companion.cube
import com.github.wleroux.automaton.component.fpscounter.FPSCounterBuilder.Companion.fpsCounter
import com.github.wleroux.automaton.component.startscreen.startScreen
import com.github.wleroux.automaton.component.window.WindowBuilder.Companion.window
import com.github.wleroux.automaton.component.window.WindowComponent
import com.github.wleroux.automaton.theme.DEFAULT_THEME
import com.github.wleroux.automaton.theme.ThemeContext
import com.github.wleroux.bus.api.DefaultBusBuilder.Companion.bus
import com.github.wleroux.bus.api.message.event.Event
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.component.overlay.OverlayBuilder.Companion.overlay
import com.github.wleroux.keact.api.mount
import com.github.wleroux.keact.api.unmount
import com.github.wleroux.keact.api.update

object GameTick: Event

fun main(args: Array<String>) {
    val bus = bus()
    val windowNode = window {
        +ThemeContext.provider {
            value = DEFAULT_THEME
            +BusContext.provider {
                value = bus
                +overlay {
                    +cube()
                    +layout {
                        +BusContext.consumer { busContext ->
                            +fpsCounter {
                                this.bus = busContext
                            }
                        }
                    }
                    +startScreen()
                }
            }
        }
    }

    val windowComponent = windowNode.mount() as WindowComponent
    while (windowComponent.isActive) {
        windowComponent.update(windowNode.properties)
        windowComponent.render()
        bus.publish(GameTick)
    }
    windowComponent.unmount()
}
