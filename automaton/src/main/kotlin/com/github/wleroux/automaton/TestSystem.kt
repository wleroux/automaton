package com.github.wleroux.automaton

import com.github.wleroux.bus.api.Bus
import com.github.wleroux.bus.api.DefaultBusBuilder.Companion.bus
import com.github.wleroux.bus.api.message.MessageHandlerBuilder.Companion.messageHandler
import com.github.wleroux.bus.api.message.command.Command
import com.github.wleroux.bus.api.message.command.DefaultCommandHandlerBuilder.Companion.commandHandler
import com.github.wleroux.bus.api.message.event.DefaultEventHandlerBuilder.Companion.eventHandler
import com.github.wleroux.bus.api.message.event.Event
import com.github.wleroux.bus.api.message.query.DefaultQueryHandler.Companion.queryHandler
import com.github.wleroux.bus.api.message.query.Query

object IncrementCommand: Command<Unit>
object CountQuery: Query<Int>
object CountIncremented: Event

class TestSystem(private val bus: Bus) {
    private var counter: Int = 0
    fun increment(cmd: IncrementCommand) {
        counter ++
        bus.publish(CountIncremented)
    }
    fun query(query: CountQuery) = counter

    fun on(event: CountIncremented) {
        println("Incremented")
    }
}

fun main() {
    val bus = bus()
    val system = TestSystem(bus)

    val subscription = bus.subscribe(messageHandler {
        +eventHandler<CountIncremented> {
            system.on(it)
        }
        +commandHandler {cmd: IncrementCommand ->
            system.increment(cmd)
        }
        +queryHandler { query: CountQuery ->
            system.query(query)
        }
    })

    repeat(5) {bus.invoke(IncrementCommand)}
    println(bus.request(CountQuery))

    subscription.close()
}