package com.github.wleroux.automaton

import com.github.wleroux.automaton.component.buttonbase.ButtonBaseBuilder.Companion.buttonBase
import com.github.wleroux.automaton.component.fpscounter.FPSCounterBuilder.Companion.fpsCounter
import com.github.wleroux.automaton.component.cube.CubeBuilder.Companion.cube
import com.github.wleroux.automaton.component.text.TextBuilder.Companion.text
import com.github.wleroux.automaton.component.window.WindowBuilder.Companion.window
import com.github.wleroux.automaton.component.window.WindowComponent
import com.github.wleroux.automaton.math.Color4f
import com.github.wleroux.keact.api.*
import com.github.wleroux.keact.api.component.layout.ContentAlign
import com.github.wleroux.keact.api.component.layout.ItemAlign
import com.github.wleroux.keact.api.component.layout.JustifyContent
import com.github.wleroux.keact.api.component.layout.LayoutBuilder.Companion.layout
import com.github.wleroux.keact.api.component.overlay.OverlayBuilder.Companion.overlay
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding

fun main(args: Array<String>) {
    val windowNode = window {
        +overlay {
            +cube()
            +layout {
                alignItems = ItemAlign.STRETCH
                alignContent = ContentAlign.STRETCH
                +fpsCounter()
                +layout {
                    alignItems = ItemAlign.CENTER
                    alignContent = ContentAlign.CENTER
                    justifyContent = JustifyContent.CENTER
                    +buttonBase {
                        +text {
                            text = "Hello, World!"
                            size = 24
                            color = Color4f(1.0f, 1.0f, 1.0f, 1.0f)
                            font = loadFont("font/Roboto_Slab/Roboto_Slab-Regular.fnt")
                        }
                    }
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
