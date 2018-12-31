package com.github.wleroux.automaton.common.program

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import java.nio.ByteBuffer

class Texture(width: Int, height: Int, pixels: ByteBuffer, internalFormat: Int = GL_RGBA16, format: Int = GL_RGBA, type: Int = GL_UNSIGNED_BYTE ) {
  private val id = glGenTextures()
  init {
    glBindTexture(GL_TEXTURE_2D, id)

    glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, pixels)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

    glBindTexture(GL_TEXTURE_2D, 0)
  }

  fun close() {
    glDeleteTextures(id)
  }

  fun bind() {
    glBindTexture(GL_TEXTURE_2D, id)
  }

  override fun toString() = String.format("Texture[$id]")
}
