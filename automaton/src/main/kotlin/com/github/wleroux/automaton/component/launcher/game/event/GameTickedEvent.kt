package com.github.wleroux.automaton.component.launcher.game.event

import com.github.wleroux.bus.api.message.event.Event

data class GameTickedEvent(val dt: Long): Event