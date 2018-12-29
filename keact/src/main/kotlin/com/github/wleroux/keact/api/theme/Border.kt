package com.github.wleroux.keact.api.theme

data class Border(val color: Color = Color.BLACK, val width: BorderWidth = BorderWidth(), val radius: BorderRadius = BorderRadius()) {
    data class BorderWidth(val top: Int = 1, val right: Int = 1, val bottom: Int = 1, val left: Int = 1)
    data class BorderRadius(val topLeft: Int = 4, val topRight: Int = 4, val bottomRight: Int = 4, val bottomLeft: Int = 4)
}
