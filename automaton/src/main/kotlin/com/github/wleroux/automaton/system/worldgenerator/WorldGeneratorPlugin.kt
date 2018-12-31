package com.github.wleroux.automaton.system.worldgenerator

import com.github.wleroux.automaton.game.Game
import com.github.wleroux.automaton.game.Plugin
import com.github.wleroux.bus.api.BusSubscription
import com.github.wleroux.bus.api.message.MessageHandlerBuilder.Companion.messageHandler
import com.github.wleroux.bus.api.message.command.DefaultCommandHandlerBuilder.Companion.commandHandler
import com.github.wleroux.bus.api.message.event.DefaultEventHandlerBuilder.Companion.eventHandler

class WorldGeneratorPlugin: Plugin {
    private lateinit var subscription: BusSubscription
    override fun initialize(game: Game) {
        val system = WorldGeneratorSystem(game)
        subscription = game.subscribe(messageHandler {
            +commandHandler(system::generate)
            +eventHandler(system::update)
        })
    }

    override fun terminate() {
        subscription.close()
    }
}