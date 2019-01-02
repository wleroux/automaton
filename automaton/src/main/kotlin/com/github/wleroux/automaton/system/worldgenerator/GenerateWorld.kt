package com.github.wleroux.automaton.system.worldgenerator

import com.github.wleroux.automaton.data.Tile
import com.github.wleroux.bus.api.message.command.Command

data class GenerateWorld(
        val seed: Long,
        val width: Int,
        val height: Int,
        val tileSettings: Map<Tile, TileSettings>
): Command<Unit>