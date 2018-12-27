package com.github.wleroux.keact.api

import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase
import kotlin.reflect.KClass

object ComponentManager {

    fun <State, Properties> unmount(component: Component<State, Properties>) {
        component.childComponents.values.forEach { unmount(it) }
        component.childComponents = emptyMap()
        component.componentWillUnmount()
    }

    fun <State, Properties> mount(type: KClass<out Component<State, Properties>>, initialProperties: Properties, parentComponent: Component<*, *>? = null): Component<State, Properties> {
        val component = type.java.newInstance()

        component.componentWillMount()
        component.parentComponent = parentComponent
        component.properties = initialProperties
        component.state = component.nextState ?: component.state
        component.componentDidMount()

        update(component, initialProperties, true)

        return component
    }

    fun <State, Properties> update(component: Component<State, Properties>, nextProperties: Properties, forceUpdate: Boolean = false) {
        fun <NodeState, NodeProperties> nodeComponent(node: Node<NodeState, NodeProperties>, prev: Component<*, *>? = null, parent: Component<*, *>? = null): Component<NodeState, NodeProperties> {
            val reuseComponent = if (prev != null) prev::class == node.type else false
            val nodeComponent: Component<NodeState, NodeProperties> = if (reuseComponent) {
                @Suppress("UNCHECKED_CAST")
                prev!! as Component<NodeState, NodeProperties>
            } else {
                mount(node.type, node.properties, parent)
            }
            update(nodeComponent, node.properties, !reuseComponent)
            return nodeComponent
        }
        fun <ChildState, ChildProperties> updateChild(component: Component<ChildState, ChildProperties>) {
            update(component, component.properties)
        }

        // Update component
        val previousProperties = component.properties
        val previousState = component.state

        if (nextProperties !== previousProperties)
            component.componentWillReceiveProps(nextProperties)
        val nextState = component.nextState ?: component.state
        if (forceUpdate || component.shouldComponentUpdate(nextProperties, nextState)) {
            component.componentWillUpdate(nextProperties, nextState)

            val previousChildren = component.childComponents
            component.properties = nextProperties
            component.state = nextState
            component.nextState = null
            var keyCounter = 0
            component.childComponents = component.asNodes().map { node ->
                val key = node.key ?: keyCounter ++
                val prevChildComponent = component.childComponents[key]
                key to nodeComponent(node, prevChildComponent, component)
            }.toMap()

            // Unmount discarded toNodes
            previousChildren.values.minus(component.childComponents.values).forEach { childComponent ->
                unmount(childComponent)
            }
            component.componentDidUpdate(previousProperties, previousState)
        } else {
            component.properties = nextProperties
            component.state = nextState
            component.childComponents.values.forEach { childComponent ->
                updateChild(childComponent)
            }
        }
    }

    fun dispatch(event: Event) {
        val ancestry = mutableListOf<Component<*, *>>()
        var child: Component<*, *>? = event.target.parentComponent
        while (child != null) {
            ancestry.add(child)
            child = child.parentComponent
        }

        // Capture phase
        event.phase = Phase.CAPTURE
        for (component in ancestry.reversed()) {
            component.handle(event)
            if (event.stopPropagation)
                return
        }

        // Target phase
        event.phase = Phase.TARGET
        event.target.handle(event)
        if (event.stopPropagation)
            return

        // Bubble phase
        event.phase = Phase.BUBBLE
        for (component in ancestry) {
            component.handle(event)
            if (event.stopPropagation)
                return
        }
    }
}