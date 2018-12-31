package com.github.wleroux.bus.api.message.event

interface EventHandler {
    fun canHandle(event: Event): Boolean
    fun handle(event: Event)
}