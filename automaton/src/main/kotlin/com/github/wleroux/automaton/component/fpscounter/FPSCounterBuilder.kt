package com.github.wleroux.automaton.component.fpscounter

import com.github.wleroux.keact.api.Node

class FPSCounterBuilder(private val key: Any?) {
    companion object {
        @JvmStatic
        fun fpsCounter(key: Any? = null, block: FPSCounterBuilder.() -> Unit = {}) = FPSCounterBuilder(key).apply(block).build()
    }

    fun build() = Node(FPSCounterComponent::class, Unit, key)
}