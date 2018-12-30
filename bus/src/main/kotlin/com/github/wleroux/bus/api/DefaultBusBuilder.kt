package com.github.wleroux.bus.api

class DefaultBusBuilder {
    companion object {
        fun bus(block: DefaultBusBuilder.() -> Unit = {}) =
                DefaultBusBuilder().apply(block).build()
    }

    fun build(): DefaultBus {
        return DefaultBus()
    }
}