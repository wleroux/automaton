package com.github.wleroux.bus.api.message.query

typealias QueryProcessor<Query, QueryResponse> = (Query) -> QueryResponse
