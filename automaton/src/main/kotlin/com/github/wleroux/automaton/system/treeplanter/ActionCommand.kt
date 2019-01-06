package com.github.wleroux.automaton.system.treeplanter

import com.github.wleroux.automaton.common.math.Vector3f
import com.github.wleroux.bus.api.message.command.Command

data class ActionCommand(
        val start: Vector3f,
        val end: Vector3f
): Command<Unit>