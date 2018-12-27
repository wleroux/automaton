package com.github.wleroux.automaton.component.greeter

import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.event.DefaultEvent

data class Click(override val target: Component<*, *>): DefaultEvent()