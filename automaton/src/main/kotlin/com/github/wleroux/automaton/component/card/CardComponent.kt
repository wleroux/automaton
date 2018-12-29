package com.github.wleroux.automaton.component.card

import com.github.wleroux.automaton.loadText
import com.github.wleroux.keact.api.theme.Color
import com.github.wleroux.automaton.math.Matrix4f.Companion.orthogonal
import com.github.wleroux.automaton.math.toFloat
import com.github.wleroux.automaton.program.Mesh
import com.github.wleroux.automaton.program.Program
import com.github.wleroux.automaton.program.Texture
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.component.padding.PaddingBuilder.Companion.padding
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import java.lang.Math.*

class CardComponent : Component<Unit, CardComponent.CardProperties>(Unit) {
  data class CardProperties(
          val theme: CardTheme = CardTheme(),
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
    val horizontalMargin = properties.theme.margin.left + properties.theme.margin.right
    val horizontalBorder = properties.theme.border.width.left + properties.theme.border.width.right
    val horizontalPadding = properties.theme.padding.left + properties.theme.padding.right
    return horizontalMargin + horizontalBorder + horizontalPadding + super.preferredWidth(parentWidth, parentHeight)
  }

  override fun preferredHeight(parentWidth: Int, parentHeight: Int): Int {
    val verticalMargin = properties.theme.margin.top + properties.theme.margin.bottom
    val verticalBorder = properties.theme.border.width.top + properties.theme.border.width.bottom
    val verticalPadding = properties.theme.padding.top + properties.theme.padding.bottom
    return verticalMargin + verticalBorder + verticalPadding + super.preferredHeight(parentWidth, parentHeight)
  }

  override fun componentDidUpdate(previousProperties: CardProperties, previousState: Unit) {
    super.componentDidUpdate(previousProperties, previousState)
    refreshCard()
  }

  private fun refreshCard() {
    val marginColor = Color(0f, 0f, 0f, 0f)
    val buttonColor = properties.theme.color
    val borderColor = properties.theme.border.color
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
            0f, 0f, 0f, 1f,
            width.toFloat(), 0f, 1f, 1f,
            width.toFloat(), height.toFloat(), 1f, 0f,
            0f, height.toFloat(), 0f, 0f
    ), arrayOf(0, 1, 2, 2, 3, 0),
            listOf(Mesh.Attribute("position", GL_FLOAT, 2), Mesh.Attribute("texCoord", GL_FLOAT, 2)))
  }

  var prevWidth: Int = 0
  var prevHeight: Int = 0
  override fun render() {
    if (prevWidth != width || prevHeight != height) {
      refreshCard()
      prevWidth = width
      prevHeight = height
    }

    program.use {
      glViewport(x, y, width, height)
      setUniform("projection", orthogonal(0f, width.toFloat(), height.toFloat(), 0f, -1f, 1f))
      setUniform("BaseColor", texture!!)
      mesh!!.bind { draw() }
    }

    val (mTop, mRight, mBottom, mLeft) = properties.theme.margin
    val (bTop, bRight, bBottom, bLeft) = properties.theme.border.width
    val (pTop, pRight, pBottom, pLeft) = properties.theme.padding
    childComponents.values.forEach {
      it.x = x + bLeft + mLeft + pLeft
      it.y = y + bBottom + mBottom + pBottom
      it.width = width - bLeft - bRight - mLeft - mRight - pLeft - pRight
      it.height = height - bTop - bBottom - mTop - mRight - pTop - pBottom
      it.render()
    }
  }

  private fun isMargin(x: Int, y: Int): Boolean {
    val margin = properties.theme.margin
    val border = properties.theme.border

    if (height - margin.top <= y || y < margin.bottom) {
      return true
    }
    if (width - margin.right <= x || x < margin.left) {
      return true
    }

    val topLeftX = margin.left + border.radius.topLeft
    val topLeftY = height - margin.top - border.radius.topLeft
    if (x <= topLeftX && y > topLeftY) {
      val d = sqrt(pow((x - topLeftX).toDouble(), 2.0) + pow((y - topLeftY).toDouble(), 2.0))
      return (d > border.radius.topLeft)
    }

    val topRightX = width - margin.right - border.radius.topRight
    val topRightY = height - margin.top - border.radius.topRight
    if (x >= topRightX && y > topRightY) {
      val d = sqrt(pow((x - topRightX).toDouble(), 2.0) + pow((y - topRightY).toDouble(), 2.0))
      return (d > border.radius.topRight)
    }

    val bottomRightX = width - margin.right - border.radius.bottomRight
    val bottomRightY = margin.bottom + border.radius.bottomRight
    if (x >= bottomRightX && y < bottomRightY) {
      val d = sqrt(pow((x - bottomRightX).toDouble(), 2.0) + pow((y - bottomRightY).toDouble(), 2.0))
      return (d > border.radius.bottomRight)
    }

    val bottomLeftX = margin.left + border.radius.bottomLeft
    val bottomLeftY = margin.bottom + border.radius.bottomLeft
    if (x <= bottomLeftX && y <= bottomLeftY) {
      val d = sqrt(pow((x - bottomLeftX).toDouble(), 2.0) + pow((y - bottomLeftY).toDouble(), 2.0))
      return (d > border.radius.bottomLeft)
    }

    return false
  }


  private fun isBorder(x: Int, y: Int): Boolean {
    val margin = properties.theme.margin
    val border = properties.theme.border

    if (height - margin.top - border.width.top <= y || y < margin.bottom + border.width.bottom) {
      return true
    }
    if (width - margin.right - border.width.right <= x || x < margin.left + border.width.left) {
      return true
    }

    val topLeftX = margin.left + border.radius.topLeft + border.width.left
    val topLeftY = height - margin.top - border.radius.topLeft - border.width.top
    if (x < topLeftX && y > topLeftY) {
      val d = sqrt(pow((x - topLeftX).toDouble(), 2.0) + pow((y - topLeftY).toDouble(), 2.0))
      return (d > border.radius.topLeft)
    }

    val topRightX = width - margin.right - border.radius.topRight - border.width.right
    val topRightY = height - margin.top - border.radius.topRight - border.width.top
    if (x > topRightX && y > topRightY) {
      val d = sqrt(pow((x - topRightX).toDouble(), 2.0) + pow((y - topRightY).toDouble(), 2.0))
      return (d > border.radius.topRight)
    }

    val bottomRightX = width - margin.right - border.radius.bottomRight - border.width.right
    val bottomRightY = margin.bottom + border.radius.bottomRight + border.width.bottom
    if (x > bottomRightX && y < bottomRightY) {
      val d = sqrt(pow((x - bottomRightX).toDouble(), 2.0) + pow((y - bottomRightY).toDouble(), 2.0))
      return (d > border.radius.bottomRight)
    }

    val bottomLeftX = margin.left + border.radius.bottomLeft + border.width.left
    val bottomLeftY = margin.bottom + border.radius.bottomLeft + border.width.bottom
    if (x < bottomLeftX && y < bottomLeftY) {
      val d = sqrt(pow((x - bottomLeftX).toDouble(), 2.0) + pow((y - bottomLeftY).toDouble(), 2.0))
      return (d > border.radius.bottomLeft)
    }

    return false
  }
}

