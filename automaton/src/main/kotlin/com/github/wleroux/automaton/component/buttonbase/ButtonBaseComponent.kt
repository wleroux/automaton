package com.github.wleroux.automaton.component.buttonbase

import com.github.wleroux.automaton.loadText
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.automaton.math.Matrix4f.Companion.orthogonal
import com.github.wleroux.automaton.math.toFloat
import com.github.wleroux.automaton.program.Mesh
import com.github.wleroux.automaton.program.Program
import com.github.wleroux.automaton.program.Texture
import com.github.wleroux.keact.api.theme.Border
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import java.lang.Math.*

class ButtonBaseComponent : Component<Unit, ButtonBaseComponent.BaseProperties>(Unit) {
  data class BaseProperties(
          val color: Color = Color(0.5f, 0.5f, 0.5f, 1.0f),
          val border: Border = Border(),
          val nodes: List<Node<*, *>>
  )

  override fun asNodes() = properties.nodes

  private lateinit var program: Program
  override fun componentDidMount() {
    program = Program.build {
      vertexShader(loadText("com/github/wleroux/automaton/program/texture/texture.vs.glsl"))
      uniform("projection")

      fragmentShader(loadText("com/github/wleroux/automaton/program/texture/texture.fs.glsl"))
      uniform("BaseColor")
    }
  }

  private var texture: Texture? = null
  private var mesh: Mesh? = null
  override fun preferredWidth(parentWidth: Int, parentHeight: Int): Int {
    val horizontalBorder = properties.border.width.left + properties.border.width.right
    return horizontalBorder + super.preferredWidth(parentWidth, parentHeight)
  }

  override fun preferredHeight(parentWidth: Int, parentHeight: Int): Int {
    val verticalBorder = properties.border.width.top + properties.border.width.bottom
    return verticalBorder + super.preferredHeight(parentWidth, parentHeight)
  }

  override fun componentDidUpdate(previousProperties: BaseProperties, previousState: Unit) {
    super.componentDidUpdate(previousProperties, previousState)
    refreshButton()
  }

  private fun refreshButton() {
    val marginColor = Color(0f, 0f, 0f, 0f)
    val buttonColor = properties.color
    val borderColor = properties.border.color
    val pixels = BufferUtils.createByteBuffer(width * height * 4)
    val pixelColors = pixels.asFloatBuffer()
    (0 until height).forEach { y ->
      (0 until width).forEach { x ->
        pixelColors.put(when {
          isMargin(x, y) -> marginColor.toFloat()
          isBorder(x, y) -> borderColor.toFloat()
          else -> buttonColor.toFloat()
        })
      }
    }

    texture?.close()
    texture = Texture(width, height, pixels, GL_RGBA8, GL_RGBA)
    mesh?.close()
    mesh = Mesh(arrayOf(
        0f, 0f, 0f, 0f,
        width.toFloat(), 0f, 1f, 0f,
        width.toFloat(), height.toFloat(), 1f, 1f,
        0f, height.toFloat(), 0f, 1f
    ), arrayOf(0, 1, 2, 2, 3, 0),
      listOf(Mesh.Attribute("position", GL_FLOAT, 2), Mesh.Attribute("texCoord", GL_FLOAT, 2)))
  }

  var prevWidth: Int = 0
  var prevHeight: Int = 0
  override fun render() {
    if (prevWidth != width && prevHeight != height) {
      refreshButton()
      prevWidth = width
      prevHeight = height
    }

    program.use {
      glViewport(x, y, width, height)
      setUniform("projection", orthogonal(0f, width.toFloat(), height.toFloat(), 0f, -1f, 1f))
      setUniform("BaseColor", texture!!)
      mesh!!.bind { draw() }
    }

    val (bTop, bRight, bBottom, bLeft) = properties.border.width
    childComponents.values.forEach {
      it.x = x + bLeft
      it.y = y + bBottom
      it.width = width - bLeft - bRight
      it.height = height - bTop - bBottom
      it.render()
    }
  }

  private fun isMargin(x: Int, y: Int): Boolean {
    val border = properties.border

    val topLeftX = border.radius.topLeft
    val topLeftY = height - border.radius.topLeft
    if (x <= topLeftX && y > topLeftY) {
      val d = sqrt(pow((x - topLeftX).toDouble(), 2.0) + pow((y - topLeftY).toDouble(), 2.0))
      return (d > border.radius.topLeft)
    }

    val topRightX = width - border.radius.topRight
    val topRightY = height - border.radius.topRight
    if (x >= topRightX && y > topRightY) {
      val d = sqrt(pow((x - topRightX).toDouble(), 2.0) + pow((y - topRightY).toDouble(), 2.0))
      return (d > border.radius.topRight)
    }

    val bottomRightX = width - border.radius.bottomRight
    val bottomRightY = border.radius.bottomRight
    if (x >= bottomRightX && y < bottomRightY) {
      val d = sqrt(pow((x - bottomRightX).toDouble(), 2.0) + pow((y - bottomRightY).toDouble(), 2.0))
      return (d > border.radius.bottomRight)
    }

    val bottomLeftX = border.radius.bottomLeft
    val bottomLeftY = border.radius.bottomLeft
    if (x <= bottomLeftX && y <= bottomLeftY) {
      val d = sqrt(pow((x - bottomLeftX).toDouble(), 2.0) + pow((y - bottomLeftY).toDouble(), 2.0))
      return (d > border.radius.bottomLeft)
    }

    return false
  }


  private fun isBorder(x: Int, y: Int): Boolean {
    val border = properties.border
    if (height - border.width.top <= y || y < border.width.bottom) {
      return true
    }
    if (width - border.width.right <= x || x < border.width.left) {
      return true
    }

    val topLeftX = border.radius.topLeft + border.width.left
    val topLeftY = height - border.radius.topLeft - border.width.top
    if (x < topLeftX && y > topLeftY) {
      val d = sqrt(pow((x - topLeftX).toDouble(), 2.0) + pow((y - topLeftY).toDouble(), 2.0))
      return (d > border.radius.topLeft)
    }

    val topRightX = width - border.radius.topRight - border.width.right
    val topRightY = height - border.radius.topRight - border.width.top
    if (x > topRightX && y > topRightY) {
      val d = sqrt(pow((x - topRightX).toDouble(), 2.0) + pow((y - topRightY).toDouble(), 2.0))
      return (d > border.radius.topRight)
    }

    val bottomRightX = width - border.radius.bottomRight - border.width.right
    val bottomRightY = border.radius.bottomRight + border.width.bottom
    if (x > bottomRightX && y < bottomRightY) {
      val d = sqrt(pow((x - bottomRightX).toDouble(), 2.0) + pow((y - bottomRightY).toDouble(), 2.0))
      return (d > border.radius.bottomRight)
    }

    val bottomLeftX = border.radius.bottomLeft + border.width.left
    val bottomLeftY = border.radius.bottomLeft + border.width.bottom
    if (x < bottomLeftX && y < bottomLeftY) {
      val d = sqrt(pow((x - bottomLeftX).toDouble(), 2.0) + pow((y - bottomLeftY).toDouble(), 2.0))
      return (d > border.radius.bottomLeft)
    }

    return false
  }
}
