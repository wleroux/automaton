package com.github.wleroux.automaton.game

import com.github.wleroux.bus.api.Bus
import com.github.wleroux.bus.api.BusSubscription
import com.github.wleroux.bus.api.DefaultBusBuilder
import com.github.wleroux.bus.api.message.MessageHandler
import com.github.wleroux.bus.api.message.command.Command
import com.github.wleroux.bus.api.message.event.Event
import com.github.wleroux.bus.api.message.query.Query
import kotlin.reflect.KClass

class Game: Bus {
    private val bus: Bus = DefaultBusBuilder.bus()
    override fun subscribe(messageHandler: MessageHandler): BusSubscription = bus.subscribe(messageHandler)
    override fun <CommandResponse> invoke(command: Command<CommandResponse>): CommandResponse = bus.invoke(command)
    override fun <QueryResponse> request(query: Query<QueryResponse>): QueryResponse = bus.request(query)
    override fun <QueryResponse> gather(query: Query<QueryResponse>): List<QueryResponse> = bus.gather(query)
    override fun publish(event: Event) = bus.publish(event)

    private val data: MutableMap<KClass<*>, MutableMap<EntityId, *>> = mutableMapOf()
    @Suppress("UNCHECKED_CAST")
    operator fun <T: Any> get(dataType: KClass<T>): MutableMap<EntityId, T> {
        return this.data.computeIfAbsent(dataType) {
            mutableMapOf<EntityId, T>()
        } as MutableMap<EntityId, T>
    }
}
