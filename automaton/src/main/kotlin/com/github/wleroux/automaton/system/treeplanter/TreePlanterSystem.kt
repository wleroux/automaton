package com.github.wleroux.automaton.system.treeplanter

import com.github.wleroux.automaton.System
import com.github.wleroux.automaton.common.math.Vector2i
import com.github.wleroux.automaton.data.ConiferousTreeTile
import com.github.wleroux.automaton.data.type.TILES
import com.github.wleroux.bus.api.BusSubscription
import com.github.wleroux.bus.api.message.MessageHandlerBuilder.Companion.messageHandler
import com.github.wleroux.bus.api.message.command.DefaultCommandHandlerBuilder.Companion.commandHandler
import com.github.wleroux.ecs.api.Game

class TreePlanterSystem: System {
    private lateinit var subscription: BusSubscription
    override fun initialize(game: Game) {
        subscription = game.subscribe(messageHandler {
            +commandHandler {actionCommand: ActionCommand ->
                var d = (actionCommand.end - actionCommand.start)
                d *= 1f / d.y
                val zeroPoint = actionCommand.start - (d * actionCommand.start.y)

                val position = Vector2i(zeroPoint.x.toInt(), zeroPoint.z.toInt())
                val currentTile = game[TILES][position.x, position.y]
                if (currentTile == ConiferousTreeTile) {
                    game[TILES][position.x, position.y] = null
                    game.publish(TreeUprootedEvent(position.x, position.y))
                } else {
                    game[TILES][position.x, position.y] = ConiferousTreeTile
                    game.publish(TreePlantedEvent(position.x, position.y))
                }
            }
        })
    }

    override fun terminate() {
        subscription.close()
    }
}