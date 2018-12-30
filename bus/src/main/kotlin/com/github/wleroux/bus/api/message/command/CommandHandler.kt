package com.github.wleroux.bus.api.message.command

interface CommandHandler {
    fun <CommandResponse> canHandle(command: Command<CommandResponse>): Boolean
    fun <CommandResponse> handle(command: Command<CommandResponse>): CommandResponse
}