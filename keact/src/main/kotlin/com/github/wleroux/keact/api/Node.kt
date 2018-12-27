package com.github.wleroux.keact.api

import kotlin.reflect.KClass

data class Node<State, Properties>(
    val type: KClass<out Component<State, Properties>>,
    val properties: Properties,
    val key: Any? = null
) {
    override fun toString(): String {
        return "Node:${type.java.simpleName}[$properties]"
    }
}