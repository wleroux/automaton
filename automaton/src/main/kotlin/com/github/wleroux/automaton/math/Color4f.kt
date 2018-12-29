package com.github.wleroux.automaton.math

import com.github.wleroux.keact.api.theme.Color
import org.lwjgl.BufferUtils


fun Color.toFloat(): Float {
    val colorBuffer = BufferUtils.createByteBuffer(4)
    colorBuffer.put((r * 255).toInt().toByte())
    colorBuffer.put((g * 255).toInt().toByte())
    colorBuffer.put((b * 255).toInt().toByte())
    colorBuffer.put((a * 255).toInt().toByte())
    colorBuffer.flip()
    return colorBuffer.asFloatBuffer().get(0)
}
