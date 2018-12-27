package com.github.wleroux.keact.api

import kotlin.reflect.KClass

interface Node<State, Properties> {
    val type: KClass<Component<State, Properties>>
    val key: Any?
    val children: List<Node<*, *>>
    val props: Properties
}