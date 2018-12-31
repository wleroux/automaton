package com.github.wleroux.automaton

import com.github.wleroux.automaton.component.launcher.LauncherBuilder.Companion.launcher
import com.github.wleroux.automaton.component.window.WindowBuilder.Companion.window
import com.github.wleroux.automaton.component.window.WindowComponent
import com.github.wleroux.automaton.component.launcher.DEFAULT_THEME
import com.github.wleroux.automaton.component.launcher.ThemeContext
import com.github.wleroux.keact.api.mount
import com.github.wleroux.keact.api.unmount
import com.github.wleroux.keact.api.update

fun main(args: Array<String>) {
    val windowNode = window {
        +launcher()
    }
    val windowComponent = windowNode.mount() as WindowComponent
    while (windowComponent.isActive) {
        windowComponent.update(windowNode.properties)
        windowComponent.render()
    }
    windowComponent.unmount()
}

