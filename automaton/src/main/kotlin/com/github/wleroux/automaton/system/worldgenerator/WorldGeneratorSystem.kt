package com.github.wleroux.automaton.system.worldgenerator

import com.github.wleroux.automaton.component.launcher.game.event.GameTickedEvent
import com.github.wleroux.automaton.game.EntityId
import com.github.wleroux.automaton.game.Game
import com.github.wleroux.automaton.game.data.PositionData
import com.github.wleroux.automaton.game.data.RenderableData
import com.github.wleroux.automaton.loadMesh
import com.github.wleroux.automaton.loadTexture
import com.github.wleroux.automaton.math.Vector2f
import kotlin.random.Random

class WorldGeneratorSystem(val game: Game) {
    @Suppress("UNUSED_PARAMETER")
    fun generate(cmd: GenerateWorld) {
        val cube = RenderableData(
                loadMesh("automaton/asset/cube.obj"),
                loadTexture("automaton/asset/Color.png")
        )


        (-2..2).forEach {x ->
            (-2..2).forEach { y ->
                val entityId = EntityId()
                game[PositionData::class][entityId] = PositionData(Vector2f(x.toFloat(), y.toFloat()))
                game[RenderableData::class][entityId] = cube
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun update(event: GameTickedEvent) {
        game[PositionData::class].forEach { _, positionData ->
            if (Random.nextInt(100) >= 99) {
                positionData.position.x += Random.nextInt(-1, 2)
                positionData.position.y += Random.nextInt(-1, 2)
            }
        }
    }
}