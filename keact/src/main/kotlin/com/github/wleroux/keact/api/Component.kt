package com.github.wleroux.keact.api

import com.github.wleroux.keact.api.event.Event

abstract class Component<State, Properties> {
    var parentComponent: Component<*, *>? = null
    var childComponents: Map<Any, Component<*, *>> = emptyMap()

    abstract var properties: Properties
    abstract var state: State
    var nextState: State? = null

    // Mounting
    fun componentWillMount(): Unit = Unit
    fun componentDidMount(): Unit = Unit
    fun componentWillUnmount(): Unit = Unit

    // Updating
    fun componentWillReceiveProps(nextProperties: Properties): Unit = Unit
    fun shouldComponentUpdate(nextProperties: Properties, nextState: State): Boolean = true
    fun componentWillUpdate(nextProperties: Properties, nextState: State): Unit = Unit
    fun componentDidUpdate(previousProperties: Properties, previousState: State): Unit = Unit

    // Nodes
    fun asNodes(): List<Node<*, *>> = emptyList()

    // Elements
    var x: Int = 0
    var y: Int = 0
    var width: Int = 0
    var height: Int = 0
    fun preferredWidth(parentWidth: Int, parentHeight: Int): Int =
            childComponents.values.map { it.preferredWidth(parentWidth, parentHeight) }.max() ?: 0
    fun preferredHeight(parentWidth: Int, parentHeight: Int): Int =
            childComponents.values.map { it.preferredHeight(parentWidth, parentHeight) }.max() ?: 0
    fun render(): Unit = childComponents.values.forEach { it.render() }
    fun handle(event: Event): Unit = Unit
    fun findComponentAt(x: Int, y: Int): Component<*, *>? {
        if (x < this.x || this.x + this.width < x) return null
        if (y < this.y || this.y + this.height < y) return null
        return childComponents.values.reversed().mapNotNull { childComponent ->
            childComponent.findComponentAt(x, y)
        }.firstOrNull() ?: this
    }
}