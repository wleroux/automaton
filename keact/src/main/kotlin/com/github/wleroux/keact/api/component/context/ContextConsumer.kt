package com.github.wleroux.keact.api.component.context

import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node

class ContextConsumer<ContextProperties: Any>: Component<Unit, ContextConsumer.ContextConsumerProperties<ContextProperties>>(Unit) {
    data class ContextConsumerProperties<ContextProperties: Any>(
        val context: Context<ContextProperties>,
        val nodes:  (ContextConsumerNodeCollector).(ContextProperties) -> Unit
    )

    private var updatePending = false
    private lateinit var contextSubscription: ContextSubscription
    override fun componentDidMount() {
        super.componentDidMount()
        contextSubscription = properties.context.subscribe {
            this.updatePending = true
        }
    }
    override fun shouldComponentUpdate(nextProperties: ContextConsumerProperties<ContextProperties>, nextState: Unit): Boolean {
        return updatePending || super.shouldComponentUpdate(nextProperties, nextState)
    }

    override fun componentWillUnmount() {
        contextSubscription.close()
    }

    override fun componentDidUpdate(previousProperties: ContextConsumerProperties<ContextProperties>, previousState: Unit) {
        updatePending = false
    }

    override fun asNodes(): List<Node<*, *>> {
        val collector = ContextConsumerNodeCollector()
        properties.nodes.invoke(collector, properties.context.value)
        return collector.build()
    }
}
