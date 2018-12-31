package com.github.wleroux.automaton.component.launcher

import com.github.wleroux.keact.api.Node

class LauncherBuilder(private val key: Any? = null) {
    companion object {
        fun launcher(key: Any? = null, block: LauncherBuilder.() -> Unit = {}) =
                LauncherBuilder(key).apply(block).build()
    }

    fun build() =
            Node(key, LauncherComponent::class, Unit)
}