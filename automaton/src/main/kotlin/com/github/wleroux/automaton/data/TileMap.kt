package com.github.wleroux.automaton.data

class TileMap(val width: Int, val depth: Int, init: (Int) -> Tile? = { null }) {
    private val backend: Array<Tile?> = Array(width * depth, init)
    operator fun get(x: Int, z: Int): Tile? {
        if (0 > x || x >= width) return null
        if (0 > z || z >= depth) return null
        return backend[z * width + x]
    }
    operator fun set(x: Int, z: Int, value: Tile?) {
        if (0 > x || x > width) throw ArrayIndexOutOfBoundsException(x)
        if (0 > z || z > depth) throw ArrayIndexOutOfBoundsException(z)
        backend[z * width + x] = value
    }
    inline fun forEach(block: (x: Int, z: Int) -> Unit) {
        (0 until width).forEach { x ->
            (0 until depth).forEach { z ->
                block(x, z)
            }
        }
    }
}