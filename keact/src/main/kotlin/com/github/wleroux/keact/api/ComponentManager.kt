package com.github.wleroux.keact.api

import com.github.wleroux.keact.api.event.Event
import com.github.wleroux.keact.api.event.Phase

fun <State, Properties> Component<State, Properties>.unmount() {
    this.childComponents.values.forEach { childComponent -> childComponent.unmount() }
    this.childComponents = emptyMap()
    this.componentWillUnmount()
}

fun <State, Properties> Node<State, Properties>.mount(parentComponent: Component<*, *>? = null): Component<State, Properties> {
    val component = this.type.java.newInstance()

    component.componentWillMount()
    component.parentComponent = parentComponent
    component.properties = this.properties
    component.state = component.nextState ?: component.state
    component.componentDidMount()

    component.update(this.properties, true)

    return component
}

fun <State, Properties> Component<State, Properties>.update(nextProperties: Properties, forceUpdate: Boolean = false) {
    fun <NodeState, NodeProperties> nodeComponent(node: Node<NodeState, NodeProperties>, prev: Component<*, *>? = null, parent: Component<*, *>? = null): Component<NodeState, NodeProperties> {
        val reuseComponent = if (prev != null) prev::class == node.type else false
        val nodeComponent: Component<NodeState, NodeProperties> = if (reuseComponent) {
            @Suppress("UNCHECKED_CAST")
            prev!! as Component<NodeState, NodeProperties>
        } else {
            node.mount(parent)
        }
        nodeComponent.update(node.properties, !reuseComponent)
        return nodeComponent
    }

    fun <ChildState, ChildProperties> updateChild(component: Component<ChildState, ChildProperties>) {
        component.update(component.properties)
    }

    // Update component
    val previousProperties = this.properties
    val previousState = this.state

    if (nextProperties !== previousProperties)
        this.componentWillReceiveProps(nextProperties)
    val nextState = this.nextState ?: this.state
    if (forceUpdate || this.shouldComponentUpdate(nextProperties, nextState)) {
        this.componentWillUpdate(nextProperties, nextState)

        val previousChildren = this.childComponents
        this.properties = nextProperties
        this.state = nextState
        this.nextState = null
        var keyCounter = 0
        this.childComponents = this.asNodes().map { node ->
            val key = node.key ?: keyCounter++
            val prevChildComponent = this.childComponents[key]
            key to nodeComponent(node, prevChildComponent, this)
        }.toMap()

        // Unmount discarded toNodes
        previousChildren.values.minus(this.childComponents.values).forEach { childComponent ->
            childComponent.unmount()
        }
        this.componentDidUpdate(previousProperties, previousState)
    } else {
        this.properties = nextProperties
        this.state = nextState
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