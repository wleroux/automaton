package com.github.wleroux.keact.api.event

import com.github.wleroux.keact.api.Component

data class Event(
    val target: Component<*, *>,
    val data: Any
) {
    var phase: Phase = Phase.CAPTURE
    var stopPropagation: Boolean = false

    override fun toString()= "$data on $target"
}