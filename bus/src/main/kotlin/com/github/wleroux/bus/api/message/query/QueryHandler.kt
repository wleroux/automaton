package com.github.wleroux.bus.api.message.query

interface QueryHandler {
    fun <QueryResponse> canHandle(query: Query<QueryResponse>): Boolean
    fun <QueryResponse> handle(query: Query<QueryResponse>): QueryResponse
    fun <QueryResponse> gather(query: Query<QueryResponse>): List<QueryResponse>
}