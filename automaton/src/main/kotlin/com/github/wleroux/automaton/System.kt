package com.github.wleroux.automaton

import com.github.wleroux.ecs.api.Game

interface System {
    fun initialize(game: Game) = Unit
    fun terminate() = Unit
}