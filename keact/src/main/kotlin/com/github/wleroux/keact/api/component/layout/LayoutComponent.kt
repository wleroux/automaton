package com.github.wleroux.keact.api.component.layout

import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import kotlin.math.max
import kotlin.math.min


/**
 * LayoutComponent takes the `bounds` provided and divides the space with to child components
 */
class LayoutComponent : Component<Unit, LayoutComponent.LayoutProperties>(Unit) {
  class LayoutProperties(
          val direction: Direction,
          val justifyContent: JustifyContent,
          val alignItems: ItemAlign,
          val alignContent: ContentAlign,
          val wrap: Wrap,
          val nodes: List<Node<*, *>>
  )

  companion object {
    /* Helper Functions */
    private val Component<*, *>.layoutItemProperties: LayoutItemComponent.LayoutItemProperties
      get() {
        val properties = this.properties
        return if (properties is LayoutItemComponent.LayoutItemProperties) properties
        else object : LayoutItemComponent.LayoutItemProperties {
          override val grow: Double = 0.0
          override val shrink: Double = 1.0
          override val alignSelf: ItemAlign? = null
        }
      }
    private fun Component<*, *>.cross(direction: Direction, width: Int, height: Int): Int {
      val preferredWidth = min(width, this.preferredWidth(width, height))
      val preferredHeight = min(height, this.preferredHeight(width, height))
      return direction.cross(preferredWidth, preferredHeight)
    }
    private fun Component<*, *>.main(direction: Direction, width: Int, height: Int): Int {
      val preferredWidth = min(width, this.preferredWidth(width, height))
      val preferredHeight = min(height, this.preferredHeight(width, height))
      return direction.main(preferredWidth, preferredHeight)
    }
  }

  override fun asNodes() = properties.nodes

  /* Attributes */
  override fun preferredWidth(parentWidth: Int, parentHeight: Int): Int {
    val componentRows = properties.wrap.split(childComponents.values.toList(), properties.direction, parentWidth, parentHeight)
    val maxMainSpace = maxMainSpace(componentRows, parentWidth, parentHeight)
    val maxCrossSpace = maxCrossSpace(componentRows, parentWidth, parentHeight)
    return properties.direction.x(maxMainSpace, maxCrossSpace)
  }
  override fun preferredHeight(parentWidth: Int, parentHeight: Int): Int {
    val componentRows = properties.wrap.split(childComponents.values.toList(), properties.direction, parentWidth, parentHeight)
    val maxMainSpace = maxMainSpace(componentRows, parentWidth, parentHeight)
    val maxCrossSpace = maxCrossSpace(componentRows, parentWidth, parentHeight)
    return properties.direction.y(maxMainSpace, maxCrossSpace)
  }
  private fun maxCrossSpace(componentRows: List<List<Component<*, *>>>, parentWidth: Int, parentHeight: Int): Int {
    var sum = 0
    componentRows.forEach { componentRow ->
      var max = 0
      componentRow.forEach { component ->
        max = max(max, component.cross(properties.direction, parentWidth, parentHeight))
      }
      sum += max
    }
    return sum
  }
  private fun maxMainSpace(componentRows: List<List<Component<*, *>>>, parentWidth: Int, parentHeight: Int): Int {
    var max = 0
    componentRows.forEach { componentRow ->
      var sum = 0
      componentRow.forEach { component ->
        sum += component.main(properties.direction, parentWidth, parentHeight)
      }
      max = max(max, sum)
    }
    return max
  }

  override fun findComponentAt(x: Int, y: Int): Component<*, *>? {
    return childComponents.values.toList().reversed().mapNotNull { it.findComponentAt(x, y) }.firstOrNull()
  }

  override fun render() {
    if (this.childComponents.isEmpty())
      return

    val mainAxisSize = properties.direction.main(width, height)
    val crossAxisSpace = properties.direction.cross(width, height)

    val itemRows = properties.wrap.split(childComponents.values.toList(), properties.direction, width, height)
    val minCrossSpace = itemRows.sumBy { row ->
      row.map {it.cross(properties.direction, width, height)}.max() ?: 0
    }

    val extraCrossSpace = crossAxisSpace - minCrossSpace
    var itemCrossOffset = properties.alignContent.componentCrossOffset(itemRows, extraCrossSpace)
    val betweenCrossOffset = properties.alignContent.betweenCrossOffset(itemRows, extraCrossSpace)
    val additionalCrossSpace = properties.alignContent.additionalCrossSpace(itemRows, extraCrossSpace)
    for (itemRow in itemRows) {
      val rowMainSpace = itemRow
              .map { it.main(properties.direction, width, height) }
              .sum()
      var rowCrossSpace = itemRow
              .map { it.cross(properties.direction, width, height) }
              .max() ?: 0
      rowCrossSpace += additionalCrossSpace

      val extraMainSpace = if (mainAxisSize > rowMainSpace) mainAxisSize - rowMainSpace else 0
      val totalGrow = itemRow.sumByDouble { it.layoutItemProperties.grow }
      val growCoefficient = if (totalGrow > 0.0) extraMainSpace / totalGrow else 0.0

      val missingMainSpace = if (mainAxisSize < rowMainSpace) rowMainSpace - mainAxisSize else 0
      val totalShrink = itemRow.sumByDouble { it.main(properties.direction, width, height) * it.layoutItemProperties.shrink }
      val shrinkCoefficient = if (totalShrink > 0.0) missingMainSpace / totalShrink else 0.0

      val initialMainOffset = properties.justifyContent.initialOffset(itemRow, if (growCoefficient > 0.0) 0 else extraMainSpace)
      val betweenMainOffset = properties.justifyContent.betweenOffset(itemRow, if (growCoefficient > 0.0) 0 else extraMainSpace)

      var itemMainOffset = initialMainOffset
      val itemIterator = properties.direction.iterator(itemRow)
      while (itemIterator.hasNext()) {
        val item = itemIterator.next()

        val grow = growCoefficient * item.layoutItemProperties.grow
        val shrink = shrinkCoefficient * (item.layoutItemProperties.shrink * item.main(properties.direction, width, height))
        val itemMainSpace = (item.main(properties.direction, width, height) + grow - shrink).toInt()
        val itemCrossSpace = item.cross(properties.direction, width, height)

        val alignSelf = item.layoutItemProperties.alignSelf ?: properties.alignItems
        val crossSpace = alignSelf.cross(itemCrossSpace, rowCrossSpace)
        val crossOffset = alignSelf.offset(itemCrossSpace, rowCrossSpace)

        item.x = x + properties.direction.x(itemMainOffset, itemCrossOffset + crossOffset)
        item.y = y + properties.direction.y(itemMainOffset, itemCrossOffset + crossOffset)
        item.width = properties.direction.x(itemMainSpace, crossSpace)
        item.height = properties.direction.y(itemMainSpace, crossSpace)
        item.render()

        itemMainOffset += itemMainSpace + betweenMainOffset
      }
      itemCrossOffset += rowCrossSpace + betweenCrossOffset
    }
  }
}