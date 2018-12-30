package com.github.wleroux.bus.api.message.event

import kotlin.reflect.KClass


class DefaultEventHandler<E: Any>(
        private val clazz: KClass<E>,
        private val eventProcessor: EventProcessor<E>
): EventHandler {
    override fun canHandle(event: Event) =
            clazz.java.isInstance(event)
    @Suppress("UNCHECKED_CAST")
    override fun handle(event: Event) =
            eventProcessor.invoke(event as E)
}
