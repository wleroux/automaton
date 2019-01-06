package com.github.wleroux.automaton.system.logger

import com.github.wleroux.bus.api.BusSubscription
import com.github.wleroux.bus.api.message.MessageHandlerBuilder.Companion.messageHandler
import com.github.wleroux.bus.api.message.event.DefaultEventHandlerBuilder.Companion.eventHandler
import com.github.wleroux.ecs.api.Game
import com.github.wleroux.automaton.System
import com.github.wleroux.automaton.component.launcher.game.event.GameTickedEvent
import com.github.wleroux.bus.api.message.event.Event

class EventLoggerSystem: System {
    private lateinit var subscription: BusSubscription
    override fun initialize(game: Game) {
        subscription = game.subscribe(messageHandler {
            +eventHandler { event: Event ->
                if (event !is GameTickedEvent) {
                    println("Event: $event")
                }
            }
        })
    }
    override fun terminate() {
        subscription.close()
    }
}