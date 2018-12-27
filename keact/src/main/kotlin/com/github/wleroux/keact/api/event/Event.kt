package com.github.wleroux.keact.api.event

import com.github.wleroux.keact.api.Component

open class Event(val target: Component<*, *>) {
    var phase: Phase = Phase.CAPTURE
    var stopPropagation: Boolean = false
}