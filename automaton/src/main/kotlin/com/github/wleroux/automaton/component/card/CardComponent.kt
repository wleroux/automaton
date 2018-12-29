package com.github.wleroux.automaton.component.card

import com.github.wleroux.automaton.loadText
import com.github.wleroux.automaton.math.Matrix4f.Companion.orthogonal
import com.github.wleroux.automaton.program.Mesh
import com.github.wleroux.automaton.program.Program
import com.github.wleroux.automaton.program.Texture
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import org.lwjgl.opengl.GL11.*

class CardComponent : Component<Unit, CardComponent.CardProperties>(Unit) {
  data class CardProperties(
          val theme: CardTheme = CardTheme(),
          val nodes: List<Node<*, *>>
  )

  override fun asNodes() = properties.nodes

  private lateinit var program: Program
  private lateinit var mesh: Mesh
  override fun componentDidMount() {
    program = Program.build {
      vertexShader(loadText("com/github/wleroux/automaton/program/card/card.vs.glsl"))
      uniform("projection")

      fragmentShader(loadText("com/github/wleroux/automaton/program/card/card.fs.glsl"))
      uniform("Width", "Height")
      uniform("Color")
      uniform("Margin.top", "Margin.right", "Margin.bottom", "Margin.left")
      uniform("Border.Color")
      uniform("Border.top", "Border.right", "Border.bottom", "Border.left")
      uniform("Border.Radius.topLeft", "Border.Radius.topRight", "Border.Radius.bottomRight", "Border.Radius.bottomLeft")
    }
    mesh = Mesh(arrayOf(
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f
    ), arrayOf(0, 1, 2, 2, 3, 0), listOf(Mesh.Attribute("posCoord", GL_FLOAT, 2)))
  }

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

  override fun render() {
    program.use {
      val margin = properties.theme.margin
      val border = properties.theme.border

      glViewport(x, y, width, height)
      setUniform("projection", orthogonal(1f, 1f, 0f, 0f, -1f, 1f))

      setUniform("Width", width)
      setUniform("Height", height)

      setUniform("Margin.top", margin.top)
      setUniform("Margin.right", margin.right)
      setUniform("Margin.bottom", margin.bottom)
      setUniform("Margin.left", margin.left)
      setUniform("Color", properties.theme.color)

      setUniform("Border.Color", border.color)

      val width = border.width
      setUniform("Border.top", width.top)
      setUniform("Border.right", width.right)
      setUniform("Border.bottom",width.bottom)
      setUniform("Border.left", width.left)

      val radius = border.radius
      setUniform("Border.Radius.topLeft", radius.topLeft)
      setUniform("Border.Radius.topRight", radius.topRight)
      setUniform("Border.Radius.bottomRight", radius.bottomRight)
      setUniform("Border.Radius.bottomLeft",radius.bottomLeft)

      mesh.bind { draw() }
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
}