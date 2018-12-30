package com.github.wleroux.bus.api

import com.github.wleroux.bus.api.message.MessageHandler
import com.github.wleroux.bus.api.message.command.Command
import com.github.wleroux.bus.api.message.event.Event
import com.github.wleroux.bus.api.message.query.Query

class DefaultBus: Bus {
    private val messageHandlers = mutableListOf<MessageHandler>()
    override fun subscribe(messageHandler: MessageHandler): BusSubscription {
        messageHandlers += messageHandler
        return object : BusSubscription {
            override fun close() {
                messageHandlers -= messageHandler
            }
        }
    }
    override fun <CommandResponse> invoke(command: Command<CommandResponse>): CommandResponse {
        val commandHandler = messageHandlers.firstOrNull { it.canHandle(command) } ?: throw IllegalArgumentException("No command handlers for: $command")
        return commandHandler.handle(command)
    }
    override fun <QueryResponse> request(query: Query<QueryResponse>): QueryResponse {
        val queryHandler = messageHandlers.firstOrNull { it.canHandle(query) } ?: throw IllegalArgumentException("No query handlers for: $query")
        return queryHandler.handle(query)
    }
    override fun <QueryResponse> gather(query: Query<QueryResponse>): List<QueryResponse> {
        return messageHandlers.filter{ it.canHandle(query) }.flatMap { it.gather(query) }
    }
    override fun publish(event: Event) {
        messageHandlers.filter {
            it.canHandle(event)
        }.forEach { it.handle(event) }
    }
}