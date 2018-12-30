package com.github.wleroux.automaton.game

interface Plugin {
    fun initialize(game: Game) = Unit
    fun terminate() = Unit
}