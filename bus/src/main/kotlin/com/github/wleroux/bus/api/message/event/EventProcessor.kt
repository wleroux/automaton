package com.github.wleroux.bus.api.message.event

typealias EventProcessor<Event> = (Event) -> Unit
