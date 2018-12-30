package com.github.wleroux.automaton.system.logger

import com.github.wleroux.automaton.component.launcher.game.event.GameTickedEvent
import com.github.wleroux.bus.api.message.event.Event

class EventLoggerSystem {
    fun onEvent(event: Event) {
        if (event !is GameTickedEvent) {
            println("Event: $event")
        }
    }
}