package com.github.wleroux.bus.api.message.command

typealias CommandProcessor<Command, CommandResponse> = (Command) -> CommandResponse
