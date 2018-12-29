package com.github.wleroux.keact.api.theme

data class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
    companion object {
        val WHITE = Color(1f, 1f, 1f, 1f)
        val BLACK = Color(0f, 0f, 0f, 1f)
    }
}