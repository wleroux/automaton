package com.github.wleroux.bus.api.message

import com.github.wleroux.bus.api.message.command.CommandHandler
import com.github.wleroux.bus.api.message.event.EventHandler
import com.github.wleroux.bus.api.message.query.QueryHandler

interface MessageHandler: CommandHandler, QueryHandler, EventHandler
