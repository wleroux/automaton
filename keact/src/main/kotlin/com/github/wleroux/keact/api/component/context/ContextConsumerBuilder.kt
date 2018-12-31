package com.github.wleroux.keact.api.component.context

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import com.github.wleroux.keact.api.component.nodecollection.NodeCollector
import kotlin.reflect.KClass

@NodeBuilderDslMarker
class ContextConsumerBuilder<ContextProperties: Any>(private val key: Any?, private val context: Context<ContextProperties>) {
    var block: NodeCollector.(ContextProperties) -> Unit = {}

    @Suppress("UNCHECKED_CAST")
    fun build() =
            Node(key, ContextConsumer::class as KClass<ContextConsumer<ContextProperties>>, ContextConsumer.ContextConsumerProperties(context, block))
}