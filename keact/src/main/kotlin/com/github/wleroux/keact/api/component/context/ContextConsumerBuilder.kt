package com.github.wleroux.keact.api.component.context

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import kotlin.reflect.KClass

@NodeBuilderDslMarker
class ContextConsumerBuilder<ContextProperties: Any>(private val context: Context<ContextProperties>, private val key: Any?) {
    var block:  (ContextConsumerNodeCollector).(ContextProperties) -> Unit = {}

    @Suppress("UNCHECKED_CAST")
    fun build() =
            Node(ContextConsumer::class as KClass<ContextConsumer<ContextProperties>>, ContextConsumer.ContextConsumerProperties(context, block), key)
}