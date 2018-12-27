package com.github.wleroux.keact.api

import kotlin.reflect.KClass


class DefaultNode<State, Properties>(override val type: KClass<Component<State, Properties>>, override val key: Any?, override val children: List<Node<*, *>>, override val props: Properties) : Node<State, Properties>
