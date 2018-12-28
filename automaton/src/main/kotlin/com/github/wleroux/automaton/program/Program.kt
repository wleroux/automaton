package com.github.wleroux.automaton.program

import com.github.wleroux.automaton.math.Color4f
import com.github.wleroux.automaton.math.Matrix4f
import com.github.wleroux.automaton.math.Quaternion
import com.github.wleroux.automaton.math.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL20.*

class Program private constructor(val id: Int, private val uniformLocations: Map<String, Int>) {
  companion object {
    fun build(block: Builder.() -> Unit): Program {
      return Builder().apply(block).build()
    }
  }
  class Builder {
    companion object {
      private const val UNKNOWN_UNIFORM_LOCATION = 0xFFFFFFFF.toInt()
    }
    private val uniforms = mutableListOf<String>()
    private val shaders = mutableMapOf<Int, String>()

    fun uniform(vararg uniform: String): Builder {
      uniforms += uniform
      return this
    }
    fun vertexShader(source: String): Builder {
      shaders[GL_VERTEX_SHADER] = source
      return this
    }
    fun fragmentShader(source: String): Builder {
      shaders[GL_FRAGMENT_SHADER] = source
      return this
    }
    fun build(): Program {
      // Create Program
      val program = glCreateProgram()
      if (program == 0) {
        throw RuntimeException("Failed to create id")
      }

      // Create & Attach Shaders
      shaders.forEach { type, source ->
        val shader = glCreateShader(type)
        if (shader == 0) {
          throw IllegalArgumentException("Could not create gl.")
        }

        glShaderSource(shader, source)
        glCompileShader(shader)
        if ( glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE) {
          throw RuntimeException("Could not compile gl: ${glGetShaderInfoLog(shader)}")
        }

        glAttachShader(program, shader)
      }

      // Link Program
      glLinkProgram(program)
      if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
        throw RuntimeException("Could not link id: ${glGetProgramInfoLog(program)}")
      }

      // Validate Program
      glValidateProgram(program)
      if (glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE) {
        throw RuntimeException("Could not validate id: ${glGetProgramInfoLog(program)}")
      }

      // Map Uniform Locations
      val uniformLocations = uniforms.associateWith { uniform ->
        val uniformLocation = glGetUniformLocation(program, uniform)
        if (uniformLocation == UNKNOWN_UNIFORM_LOCATION) {
          throw IllegalArgumentException("Could not find uniform $uniform")
        }
        uniformLocation
      }

      return Program(program, uniformLocations)
    }
  }

  fun setUniform(uniform: String, value: Int) {
    glUniform1i(uniformLocations[uniform]!!, value)
  }
  fun setUniform(uniform: String, value: Float) {
    glUniform1f(uniformLocations[uniform]!!, value)
  }
  fun setUniform(uniform: String, value: Vector3f) {
    glUniform3f(uniformLocations[uniform]!!, value.x, value.y, value.z)
  }
  fun setUniform(uniform: String, value: Quaternion) {
    glUniform4f(uniformLocations[uniform]!!, value.x, value.y, value.z, value.w)
  }
  fun setUniform(uniform: String, value: Color4f) {
    glUniform4f(uniformLocations[uniform]!!, value.r, value.g, value.b, value.a)
  }
  fun setUniform(uniform: String, value: Matrix4f) {
    val buffer = BufferUtils.createFloatBuffer(16)
    value.store(buffer)
    buffer.flip()
    glUniformMatrix4fv(uniformLocations[uniform]!!, false, buffer)
  }
  fun setUniform(uniform: String, value: Texture) {
    glUniform1i(uniformLocations[uniform]!!, uniformLocations[uniform]!!)
    glActiveTexture(GL_TEXTURE0 + uniformLocations[uniform]!!)
    value.bind()
  }

  inline fun use(block: Program.()->Unit) {
    glUseProgram(id)
    block.invoke(this)
    glUseProgram(0)
  }

  override fun toString(): String = this.javaClass.simpleName
}
