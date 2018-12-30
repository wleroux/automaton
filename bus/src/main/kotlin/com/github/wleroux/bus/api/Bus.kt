package com.github.wleroux.bus.api

import com.github.wleroux.bus.api.message.MessageHandler
import com.github.wleroux.bus.api.message.command.Command
import com.github.wleroux.bus.api.message.event.Event
import com.github.wleroux.bus.api.message.query.Query

interface Bus {
    fun subscribe(messageHandler: MessageHandler): BusSubscription
    fun <CommandResponse> invoke(command: Command<CommandResponse>): CommandResponse
    fun <QueryResponse> request(query: Query<QueryResponse>): QueryResponse
    fun publish(event: Event)
}

