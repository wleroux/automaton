package com.github.wleroux.bus.api.message.command

import kotlin.reflect.KClass

class DefaultCommandHandler<R, C: Command<R>>(
        private val clazz: KClass<C>,
        private val commandProcessor: CommandProcessor<C, R>
): CommandHandler {
    override fun <CommandResponse> canHandle(command: Command<CommandResponse>) =
            clazz.java.isInstance(command)
    @Suppress("UNCHECKED_CAST")
    override fun <CommandResponse> handle(command: Command<CommandResponse>) =
            commandProcessor.invoke(command as C) as CommandResponse
}

