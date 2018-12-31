package com.github.wleroux.keact.api

import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase

fun <State: Any, Properties: Any> Component<State, Properties>.unmount() {
    childComponents.values.forEach { childComponent -> childComponent.unmount() }
    childComponents = emptyMap()
    componentWillUnmount()
}

fun <State: Any, Properties: Any> Node<State, Properties>.mount(parentComponent: Component<*, *>? = null): Component<State, Properties> {
    val component = type.java.newInstance()

    component.componentWillMount()
    component.parentComponent = parentComponent
    component.properties = properties
    component.componentDidMount()

    component.update(component.properties, true)

    return component
}

fun <State: Any, Properties: Any> Component<State, Properties>.update(nextProperties: Properties, forceUpdate: Boolean = false) {
    fun <NodeState: Any, NodeProperties: Any> nodeComponent(node: Node<NodeState, NodeProperties>, prev: Component<*, *>? = null): Component<NodeState, NodeProperties> {
        val reuseComponent = if (prev != null) prev::class == node.type else false
        val nodeComponent = if (reuseComponent) {
            @Suppress("UNCHECKED_CAST")
            prev!! as Component<NodeState, NodeProperties>
        } else {
            node.mount(this@update)
        }
        nodeComponent.update(node.properties)
        return nodeComponent
    }

    fun <ChildState: Any, ChildProperties: Any> updateChild(component: Component<ChildState, ChildProperties>) {
        component.update(component.properties)
    }

    // Update component
    val previousProperties = properties
    if (nextProperties !== previousProperties)
        componentWillReceiveProps(nextProperties)
    if (forceUpdate || shouldComponentUpdate(nextProperties, state)) {
        componentWillUpdate(nextProperties, state)

        val previousChildren = childComponents
        properties = nextProperties
        var keyCounter = 0
        childComponents = asNodes().map { node ->
            val key = node.key ?: keyCounter++
            val prevChildComponent = childComponents[key]
            key to nodeComponent(node, prevChildComponent)
        }.toMap()

        // Unmount discarded toNodes
        previousChildren.values.minus(childComponents.values).forEach { childComponent ->
            childComponent.unmount()
        }
        componentDidUpdate(previousProperties, previousState)
        previousState = state
    } else {
        properties = nextProperties
        previousState = state
        childComponents.values.forEach { childComponent ->
            updateChild(childComponent)
        }
    }
}

fun Component<*, *>.dispatch(data: Any) {
    val event = Event(this, data)

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