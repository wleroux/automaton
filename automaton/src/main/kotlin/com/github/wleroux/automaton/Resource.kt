package com.github.wleroux.automaton

import com.github.wleroux.automaton.component.text.font.BitmapFontReader
import com.github.wleroux.automaton.component.text.font.Font
import com.github.wleroux.automaton.program.Mesh
import com.github.wleroux.automaton.program.Texture
import com.github.wleroux.automaton.program.format.WavefrontObjReader
import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

fun loadText(file: String): String {
    val classLoader = Thread.currentThread().contextClassLoader
    classLoader.getResourceAsStream(file).use { inputStream ->
        return inputStream.readBytes().toString(Charsets.UTF_8)
    }
}

fun loadMesh(file: String): Mesh {
   return WavefrontObjReader.read(loadText(file))
}

fun loadFont(file: String): Font {
    return BitmapFontReader.read(file)
}

fun loadTexture(file: String): Texture {
    MemoryStack.stackPush().use { stack ->
        val width = stack.mallocInt(1)
        val height = stack.mallocInt(1)
        val components = stack.mallocInt(1)

        val byteBuffer = loadByteBuffer(file)
        require(stbi_info_from_memory(byteBuffer, width, height, components)) {
            "Failed to read image information: ${stbi_failure_reason()}"
        }

        stbi_set_flip_vertically_on_load(true)
        val imageBuffer = stbi_load_from_memory(byteBuffer, width, height, components, 4)!!
        return Texture(width[0], height[0], imageBuffer)
    }
}

private fun loadByteBuffer(file: String): ByteBuffer {
    val classLoader = Thread.currentThread().contextClassLoader
    classLoader.getResourceAsStream(file).use { inputStream ->
        val byteArray = inputStream.readBytes()
        val byteBuffer = BufferUtils.createByteBuffer(byteArray.size)
        byteBuffer.put(byteArray)
        byteBuffer.flip()
        return byteBuffer
    }
}
