package com.github.wleroux.keact.api.event

import com.github.wleroux.keact.api.Component

interface Event {
    val target: Component<*, *>
    var phase: Phase
    var stopPropagation: Boolean
}

abstract class DefaultEvent: Event {
    override var phase: Phase = Phase.CAPTURE
    override var stopPropagation: Boolean = false
}