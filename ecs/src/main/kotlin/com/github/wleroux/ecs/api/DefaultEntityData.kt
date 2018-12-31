package com.github.wleroux.ecs.api

class DefaultEntityData<T>: EntityData<T> {
    private val backend = mutableMapOf<EntityId, T>()
    override fun iterator() = backend.iterator()
    override fun get(entityId: EntityId): T? = backend[entityId]
    override fun set(entityId: EntityId, value: T?): Unit {
        if (value == null) backend.remove(entityId)
        else backend[entityId] = value
    }
}