package com.github.wleroux.ecs.api

interface EntityData<T>: MutableIterable<MutableMap.MutableEntry<EntityId, T>> {
    operator fun get(entityId: EntityId): T?
    operator fun set(entityId: EntityId, value: T?)
}