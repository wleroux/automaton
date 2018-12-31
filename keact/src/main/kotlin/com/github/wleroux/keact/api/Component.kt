package com.github.wleroux.keact.api

import com.github.wleroux.keact.api.event.Event

abstract class Component<State: Any, Properties: Any>(initialState: State) {
    var parentComponent: Component<*, *>? = null
    var childComponents: Map<Any, Component<*, *>> = emptyMap()

    lateinit var properties: Properties
    var previousState: State = initialState
    var state: State = initialState

    // Mounting
    open fun componentWillMount(): Unit = Unit
    open fun componentDidMount(): Unit = Unit
    open fun componentWillUnmount(): Unit = Unit

    // Updating
    open fun componentWillReceiveProps(nextProperties: Properties): Unit = Unit
    open fun shouldComponentUpdate(nextProperties: Properties, nextState: State): Boolean {
        return properties != nextProperties || previousState != nextState
    }
    open fun componentWillUpdate(nextProperties: Properties, nextState: State): Unit = Unit
    open fun componentDidUpdate(previousProperties: Properties, previousState: State): Unit = Unit

    // Nodes
    open fun asNodes(): List<Node<*, *>> = emptyList()

    // Rendering
    var x: Int = 0
    var y: Int = 0
    var width: Int = 0
    var height: Int = 0
    open fun preferredWidth(parentWidth: Int, parentHeight: Int): Int =
            childComponents.values.map { it.preferredWidth(parentWidth, parentHeight) }.max() ?: 0
    open fun preferredHeight(parentWidth: Int, parentHeight: Int): Int =
            childComponents.values.map { it.preferredHeight(parentWidth, parentHeight) }.max() ?: 0
    open fun render(): Unit = childComponents.values.forEach {
        it.x = x
        it.y = y
        it.width = width
        it.height = height
        it.render()
    }

    // Handling Events
    open fun handle(event: Event): Unit = Unit

    // Retrieving Components
    open fun findComponentAt(x: Int, y: Int): Component<*, *>? {
        if (x < this.x || this.x + this.width < x) return null
        if (y < this.y || this.y + this.height < y) return null
        return childComponents.values.reversed().mapNotNull { childComponent ->
            childComponent.findComponentAt(x, y)
        }.firstOrNull() ?: this
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}[$properties]($childComponents)"
    }
}