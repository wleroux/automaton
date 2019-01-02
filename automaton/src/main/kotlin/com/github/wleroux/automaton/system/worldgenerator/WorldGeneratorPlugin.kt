package com.github.wleroux.automaton.system.worldgenerator

import com.github.wleroux.bus.api.BusSubscription
import com.github.wleroux.bus.api.message.MessageHandlerBuilder.Companion.messageHandler
import com.github.wleroux.bus.api.message.command.DefaultCommandHandlerBuilder.Companion.commandHandler
import com.github.wleroux.ecs.api.Game
import com.github.wleroux.automaton.Plugin
import com.github.wleroux.automaton.common.math.perlin_noise
import com.github.wleroux.automaton.data.TILES
import com.github.wleroux.automaton.data.TileMap
import kotlin.random.Random

class WorldGeneratorPlugin: Plugin {
    private lateinit var subscription: BusSubscription
    override fun initialize(game: Game) {
        subscription = game.subscribe(messageHandler {
            +commandHandler { cmd: GenerateWorld ->
                game[TILES] = TileMap(cmd.width, cmd.height)

                val random = Random(cmd.seed)
                cmd.tileSettings.forEach { tile, tileSettings ->
                    val seed = random.nextLong()
                    game[TILES].forEach { x, z ->
                        val noise = perlin_noise(
                                seed,
                                x.toFloat() * tileSettings.frequency,
                                0f * tileSettings.frequency,
                                z.toFloat() * tileSettings.frequency,
                                lacunarity = 0.5f,
                                gain = 0.5f,
                                octaves = 3
                        )
                        if (noise < tileSettings.size) {
                            game[TILES][x, z] = tile
                        }
                    }
                }
                Unit
            }
        })
    }

    override fun terminate() {
        subscription.close()
    }
}