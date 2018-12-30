package com.github.wleroux.bus.api.message.command

class DefaultCommandHandlerBuilder {
    companion object {
        inline fun <CommandResponse, reified C : Command<CommandResponse>> commandHandler(noinline commandProcessor: CommandProcessor<C, CommandResponse>): DefaultCommandHandler<CommandResponse, C> {
            return DefaultCommandHandler(C::class, commandProcessor)
        }
    }
}
