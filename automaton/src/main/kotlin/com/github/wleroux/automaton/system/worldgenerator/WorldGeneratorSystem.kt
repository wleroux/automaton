package com.github.wleroux.automaton.system.worldgenerator

import com.github.wleroux.automaton.component.launcher.game.event.GameTickedEvent
import com.github.wleroux.automaton.data.POSITION
import com.github.wleroux.automaton.data.RENDERABLE
import com.github.wleroux.automaton.loadMesh
import com.github.wleroux.automaton.loadTexture
import com.github.wleroux.automaton.common.math.Vector2f
import com.github.wleroux.automaton.common.program.Material
import com.github.wleroux.automaton.common.program.Model
import com.github.wleroux.ecs.api.DefaultEntityData
import com.github.wleroux.ecs.api.EntityId
import com.github.wleroux.ecs.api.Game
import kotlin.random.Random

class WorldGeneratorSystem(val game: Game) {
    @Suppress("UNUSED_PARAMETER")
    fun generate(cmd: GenerateWorld) {
        game[POSITION] = DefaultEntityData()
        game[RENDERABLE] = DefaultEntityData()

        val mesh =loadMesh("automaton/asset/cube.obj")
        val texture = loadTexture("automaton/asset/Color.png")

        val cubePrototype = { game: Game ->
            val entityId = EntityId()
            game[RENDERABLE][entityId] = Model(mesh, Material(texture))
            entityId
        }

        (-2..2).forEach {x ->
            (-2..2).forEach { y ->
                cubePrototype(game).also { entityId ->
                    game[POSITION][entityId] = Vector2f(x.toFloat(), y.toFloat())
                }
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun update(event: GameTickedEvent) {
        game[POSITION].forEach { (_, position) ->
            if (Random.nextInt(100) >= 99) {
                position.x += Random.nextInt(-1, 2)
                position.y += Random.nextInt(-1, 2)
            }
        }
    }
}