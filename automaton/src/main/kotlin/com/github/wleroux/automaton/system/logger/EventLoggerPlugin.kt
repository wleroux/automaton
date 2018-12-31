package com.github.wleroux.automaton.system.logger

import com.github.wleroux.bus.api.BusSubscription
import com.github.wleroux.bus.api.message.MessageHandlerBuilder.Companion.messageHandler
import com.github.wleroux.bus.api.message.event.DefaultEventHandlerBuilder.Companion.eventHandler
import com.github.wleroux.ecs.api.Game
import com.github.wleroux.automaton.Plugin

class EventLoggerPlugin: Plugin {
    private lateinit var subscription: BusSubscription
    override fun initialize(game: Game) {
        val eventLoggerSystem = EventLoggerSystem()
        subscription = game.subscribe(messageHandler {
            +eventHandler(eventLoggerSystem::onEvent)
        })
    }
    override fun terminate() {
        subscription.close()
    }
}