package com.github.wleroux.bus.api.message.query

class DefaultQueryHandlerBuilder {
    companion object {
        inline fun <QueryResponse, reified Q: Query<QueryResponse>> queryHandler(noinline queryProcessor: QueryProcessor<Q, QueryResponse>): DefaultQueryHandler<QueryResponse, Q> {
            return DefaultQueryHandler(Q::class, queryProcessor)
        }
    }
}