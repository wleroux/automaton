package com.github.wleroux.automaton

import com.github.wleroux.ecs.api.Game

interface Plugin {
    fun initialize(game: Game) = Unit
    fun terminate() = Unit
}