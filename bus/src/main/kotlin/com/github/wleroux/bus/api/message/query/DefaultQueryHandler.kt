package com.github.wleroux.bus.api.message.query

import kotlin.reflect.KClass

class DefaultQueryHandler<R, Q: Query<R>>(
        private val clazz: KClass<Q>,
        private val queryProcessor: QueryProcessor<Q, R>
): QueryHandler {
    companion object {
        inline fun <QueryResponse, reified Q: Query<QueryResponse>> queryHandler(noinline queryProcessor: QueryProcessor<Q, QueryResponse>): DefaultQueryHandler<QueryResponse, Q> {
            return DefaultQueryHandler(Q::class, queryProcessor)
        }
    }
    override fun <CommandResponse> canHandle(query: Query<CommandResponse>) =
            clazz.java.isInstance(query)
    @Suppress("UNCHECKED_CAST")
    override fun <QueryResponse> handle(command: Query<QueryResponse>) =
            queryProcessor.invoke(command as Q) as QueryResponse
}