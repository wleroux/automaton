package com.github.wleroux.keact.api.theme

data class Border(val color: Color = Color.BLACK, val width: BorderWidth = BorderWidth(), val radius: BorderRadius = BorderRadius()) {
    data class BorderWidth(val top: Int = 0, val right: Int = 0, val bottom: Int = 0, val left: Int = 0)
    data class BorderRadius(val topLeft: Int = 0, val topRight: Int = 0, val bottomRight: Int = 0, val bottomLeft: Int = 0)
}
