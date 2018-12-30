package com.github.wleroux.bus.api.message

import com.github.wleroux.bus.api.message.command.CommandHandler
import com.github.wleroux.bus.api.message.event.EventHandler
import com.github.wleroux.bus.api.message.query.QueryHandler

@MessageHandlerDslMarker
class MessageHandlerBuilder {
    companion object {
        fun messageHandler(block: MessageHandlerBuilder.() -> Unit) =
                MessageHandlerBuilder().apply(block).build()
    }

    private val commandHandlers = mutableListOf<CommandHandler>()
    private val queryHandlers = mutableListOf<QueryHandler>()
    private val eventHandlers = mutableListOf<EventHandler>()

    operator fun MessageHandler.unaryPlus() {
        commandHandlers += this
        queryHandlers += this
        eventHandlers += this
    }

    operator fun CommandHandler.unaryPlus() {
        commandHandlers += this
    }
    operator fun QueryHandler.unaryPlus() {
        queryHandlers += this
    }
    operator fun EventHandler.unaryPlus() {
        eventHandlers += this
    }

    fun build() = DefaultMessageHandler(commandHandlers, queryHandlers, eventHandlers)
}