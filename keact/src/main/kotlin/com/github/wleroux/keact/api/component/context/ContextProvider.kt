package com.github.wleroux.keact.api.component.context

import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node

class ContextProvider<ContextProperties: Any>: Component<Unit, ContextProvider.ProviderProperties<ContextProperties>>(Unit) {
    data class ProviderProperties<ContextProperties: Any>(
            val context: Context<ContextProperties>,
            val value: ContextProperties,
            val nodes: List<Node<*, *>>
    )

    override fun asNodes() = properties.nodes
    override fun componentDidMount() {
        properties.context.update(properties.value)
    }
    override fun componentWillReceiveProps(nextProperties: ProviderProperties<ContextProperties>) {
        properties.context.update(nextProperties.value)
    }

    override fun shouldComponentUpdate(nextProperties: ProviderProperties<ContextProperties>, nextState: Unit): Boolean {
        return properties.nodes != nextProperties.nodes
    }
}