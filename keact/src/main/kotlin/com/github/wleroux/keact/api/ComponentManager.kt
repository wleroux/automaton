package com.github.wleroux.keact.api

import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase

fun <State: Any, Properties: Any> Component<State, Properties>.unmount() {
    this.childComponents.values.forEach { childComponent -> childComponent.unmount() }
    this.childComponents = emptyMap()
    this.componentWillUnmount()
}

fun <State: Any, Properties: Any> Node<State, Properties>.mount(parentComponent: Component<*, *>? = null): Component<State, Properties> {
    val component = this.type.java.newInstance()

    component.componentWillMount()
    component.parentComponent = parentComponent
    component.properties = this.properties
    component.componentDidMount()

    component.update(component.properties, true)

    return component
}

fun <State: Any, Properties: Any> Component<State, Properties>.update(nextProperties: Properties, forceUpdate: Boolean = false) {
    fun <NodeState: Any, NodeProperties: Any> nodeComponent(node: Node<NodeState, NodeProperties>, prev: Component<*, *>? = null, parent: Component<*, *>? = null): Component<NodeState, NodeProperties> {
        val reuseComponent = if (prev != null) prev::class == node.type else false
        val nodeComponent: Component<NodeState, NodeProperties> = if (reuseComponent) {
            @Suppress("UNCHECKED_CAST")
            prev!! as Component<NodeState, NodeProperties>
        } else {
            node.mount(parent)
        }
        nodeComponent.update(node.properties)
        return nodeComponent
    }

    fun <ChildState: Any, ChildProperties: Any> updateChild(component: Component<ChildState, ChildProperties>) {
        component.update(component.properties)
    }

    // Update component
    val previousProperties = this.properties
    if (nextProperties !== previousProperties)
        this.componentWillReceiveProps(nextProperties)
    if (forceUpdate || this.shouldComponentUpdate(nextProperties, state)) {
        this.componentWillUpdate(nextProperties, state)

        val previousChildren = this.childComponents
        this.properties = nextProperties
        var keyCounter = 0
        this.childComponents = this.asNodes().map { node ->
            val key = node.key ?: keyCounter++
            val prevChildComponent = this.childComponents[key]
            key to nodeComponent(node as Node<Any, Any>, prevChildComponent, this)
        }.toMap()

        // Unmount discarded toNodes
        previousChildren.values.minus(this.childComponents.values).forEach { childComponent ->
            childComponent.unmount()
        }
        this.componentDidUpdate(previousProperties, previousState)
        this.previousState = state
    } else {
        this.properties = nextProperties
        this.previousState = state
        this.childComponents.values.forEach { childComponent ->
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