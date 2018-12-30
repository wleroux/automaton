package com.github.wleroux.automaton.component.launcher.fpscounter

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class FPSCounterBuilder(private val key: Any?) {
    companion object {
        @JvmStatic
        fun fpsCounter(key: Any? = null, block: FPSCounterBuilder.() -> Unit = {}) = FPSCounterBuilder(key).apply(block).build()
    }

    fun build() = Node(FPSCounterComponent::class, Unit, key)
}