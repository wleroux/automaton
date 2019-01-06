package com.github.wleroux.automaton.system.treeplanter

import com.github.wleroux.bus.api.message.event.Event

data class TreeUprootedEvent(
        val x: Int,
        val z: Int
): Event