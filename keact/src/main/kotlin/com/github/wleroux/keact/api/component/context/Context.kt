package com.github.wleroux.keact.api.component.context

class Context<ContextProperties: Any> {
    lateinit var value: ContextProperties
        private set

    val listeners = mutableListOf<(ContextProperties) -> Unit>()
    fun update(value: ContextProperties) {
        this.value = value
        listeners.forEach { it.invoke(value) }
    }

    fun subscribe(listener: (ContextProperties) -> Unit): ContextSubscription {
        listeners += listener
        return object : ContextSubscription {
            override fun close() {
                listeners -= listener
            }
        }
    }

    fun provider(key: Any? = null, block: ContextProviderBuilder<ContextProperties>.() -> Unit) =
            ContextProviderBuilder(this, key).apply(block).build()
    fun consumer(key: Any? = null, block: (ContextConsumerNodeCollector).(ContextProperties) -> Unit) =
            ContextConsumerBuilder(this, key).apply {
                this.block = block
            }.build()
}