package com.github.wleroux.automaton

import com.github.wleroux.automaton.component.buttonbase.ButtonBaseBuilder.Companion.buttonBase
import com.github.wleroux.automaton.component.buttonbase.ButtonBaseComponent
import com.github.wleroux.automaton.component.fpscounter.FPSCounterBuilder.Companion.fpsCounter
import com.github.wleroux.automaton.component.cube.CubeBuilder.Companion.cube
import com.github.wleroux.automaton.component.mainmenu.MainMenuBuilder.Companion.mainMenu
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.component.window.WindowBuilder.Companion.window
import com.github.wleroux.automaton.component.window.WindowComponent
import com.github.wleroux.keact.api.*
import com.github.wleroux.keact.api.component.layout.ContentAlign
import com.github.wleroux.keact.api.component.layout.ItemAlign
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.component.overlay.OverlayBuilder.Companion.overlay
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.theme.Color

fun main(args: Array<String>) {
    val windowNode = window {
        +overlay {
            +cube()
            +layout {
                alignItems = ItemAlign.CENTER
                alignContent = ContentAlign.CENTER
                +fpsCounter()
                +buttonBase {
                    color = Color(0.4f, 0f, 0f, 1f)
                    border = Border(
                            color = Color(1f, 0f, 0f, 1f),
                            width = Border.BorderWidth(
                                    2, 2, 2, 2
                            ),
                            radius = Border.BorderRadius(
                                    2, 2, 2, 2
                            )
                    )
                    +padding {
                        +text {
                            text = "Hello, World1"
                        }
                    }
                }
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
