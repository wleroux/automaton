package com.github.wleroux.bus.api.message

import com.github.wleroux.bus.api.message.command.Command
import com.github.wleroux.bus.api.message.command.CommandHandler
import com.github.wleroux.bus.api.message.event.Event
import com.github.wleroux.bus.api.message.event.EventHandler
import com.github.wleroux.bus.api.message.query.Query
import com.github.wleroux.bus.api.message.query.QueryHandler

class DefaultMessageHandler(
        private val commandHandlers: List<CommandHandler>,
        private val queryHandlers: List<QueryHandler>,
        private val eventHandlers: List<EventHandler>
): MessageHandler {
    override fun <CommandResponse> canHandle(command: Command<CommandResponse>) = commandHandlers.any { it.canHandle(command) }
    override fun <CommandResponse> handle(command: Command<CommandResponse>) = commandHandlers.first { it.canHandle(command) }.handle(command)
    override fun <QueryResponse> canHandle(query: Query<QueryResponse>) = queryHandlers.any { it.canHandle(query) }
    override fun <QueryResponse> handle(query: Query<QueryResponse>): QueryResponse = queryHandlers.first { it.canHandle(query) }.handle(query)
    override fun <QueryResponse> gather(query: Query<QueryResponse>): List<QueryResponse> = queryHandlers.flatMap { it.gather(query) }
    override fun canHandle(event: Event) = eventHandlers.any { it.canHandle(event) }
    override fun handle(event: Event) = eventHandlers.filter { it.canHandle(event) }.forEach { it.handle(event) }
}

