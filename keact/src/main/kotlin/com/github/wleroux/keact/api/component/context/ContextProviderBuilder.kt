package com.github.wleroux.keact.api.component.context

import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.NodeBuilderDslMarker
import kotlin.reflect.KClass

@NodeBuilderDslMarker
class ContextProviderBuilder<ContextProperties: Any>(private val context: Context<ContextProperties>, private val key: Any? = null) {
    lateinit var value: ContextProperties
    private val nodes = mutableListOf<Node<*, *>>()
    operator fun Node<*, *>.unaryPlus() {
        nodes += this
    }

    @Suppress("UNCHECKED_CAST")
    fun build(key: Any? = null) =
            Node(ContextProvider::class as KClass<ContextProvider<ContextProperties>>, ContextProvider.ProviderProperties(context, value, nodes), key)
}