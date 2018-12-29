package com.github.wleroux.automaton

import com.github.wleroux.automaton.component.fpscounter.FPSCounterBuilder.Companion.fpsCounter
import com.github.wleroux.automaton.component.cube.CubeBuilder.Companion.cube
import com.github.wleroux.automaton.component.mainmenu.MainMenuBuilder.Companion.mainMenu
import com.github.wleroux.automaton.component.window.WindowBuilder.Companion.window
import com.github.wleroux.automaton.component.window.WindowComponent
import com.github.wleroux.keact.api.*
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.component.overlay.OverlayBuilder.Companion.overlay

fun main(args: Array<String>) {
    val windowNode = window {
        +overlay {
            +cube()
            +layout {
                +fpsCounter()
            }
            +mainMenu {
                startGameHandler = {
                    println("Starting!")
                }
                loadGameHandler = {
                    println("Loading")
                }
                quitHandler = {
                    System.exit(1)
                }
            }
        }
    }

    val windowComponent = windowNode.mount() as WindowComponent
    while (windowComponent.isActive) {
        windowComponent.update(windowNode.properties)
        windowComponent.render()
    }
    windowComponent.unmount()
}
