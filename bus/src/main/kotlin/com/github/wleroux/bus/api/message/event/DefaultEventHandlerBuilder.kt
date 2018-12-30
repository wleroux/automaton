package com.github.wleroux.bus.api.message.event

class DefaultEventHandlerBuilder {
    companion object {
        inline fun <reified E: Event> eventHandler(noinline eventProcessor: EventProcessor<E>): DefaultEventHandler<E> {
            return DefaultEventHandler(E::class, eventProcessor)
        }
    }
}
