package com.github.wleroux.automaton.component.fpscounter

import com.github.wleroux.bus.api.Bus
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker

@NodeBuilderDslMarker
class FPSCounterBuilder(private val key: Any?) {
    companion object {
        @JvmStatic
        fun fpsCounter(key: Any? = null, block: FPSCounterBuilder.() -> Unit = {}) = FPSCounterBuilder(key).apply(block).build()
    }

    lateinit var bus: Bus

    fun build() = Node(FPSCounterComponent::class, bus, key)
}