package com.github.wleroux.ecs.api

import com.github.wleroux.bus.api.Bus
import com.github.wleroux.bus.api.BusSubscription
import com.github.wleroux.bus.api.DefaultBusBuilder
import com.github.wleroux.bus.api.message.MessageHandler
import com.github.wleroux.bus.api.message.command.Command
import com.github.wleroux.bus.api.message.event.Event
import com.github.wleroux.bus.api.message.query.Query

class Game: Bus {
    private val bus: Bus = DefaultBusBuilder.bus()
    override fun subscribe(messageHandler: MessageHandler): BusSubscription = bus.subscribe(messageHandler)
    override fun <CommandResponse> invoke(command: Command<CommandResponse>): CommandResponse = bus.invoke(command)
    override fun <QueryResponse> request(query: Query<QueryResponse>): QueryResponse = bus.request(query)
    override fun <QueryResponse> gather(query: Query<QueryResponse>): List<QueryResponse> = bus.gather(query)
    override fun publish(event: Event) = bus.publish(event)

    private val state: MutableMap<DataType<*>, Any> = mutableMapOf()
    @Suppress("UNCHECKED_CAST")
    operator fun <T: Any> get(dataType: DataType<T>): T {
        return this.state[dataType] as T
    }
    operator fun <T: Any> set(dataType: DataType<T>, value: T?) {
        if (value == null) state.remove(dataType)
        else state[dataType] = value
    }
}
