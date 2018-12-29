package com.github.wleroux.keact.api.theme

data class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
    companion object {
        val WHITE = Color(0xffffff)
        val BLACK = Color(0x000000)
        val BLUEGREY_50: Color = Color(0xeceff1)
        val BLUEGREY_100: Color = Color(0xcfd8dc)
        val BLUEGREY_200: Color = Color(0xb0bec5)
        val BLUEGREY_300: Color = Color(0x90a4ae)
        val BLUEGREY_400: Color = Color(0x78909c)
        val BLUEGREY_500: Color = Color(0x607d8b)
        val BLUEGREY_600: Color = Color(0x546e7a)
        val BLUEGREY_700: Color = Color(0x455a64)
        val BLUEGREY_800: Color = Color(0x37474f)
        val BLUEGREY_900: Color = Color(0x263238)

        val ORANGE_50: Color = Color(0xfbe9e7)
        val ORANGE_100: Color = Color(0xffccbc)
        val ORANGE_200: Color = Color(0xffab91)
        val ORANGE_300: Color = Color(0xff8a65)
        val ORANGE_400: Color = Color(0xff7043)
        val ORANGE_500: Color = Color(0xff5722)
        val ORANGE_600: Color = Color(0xf4511e)
        val ORANGE_700: Color = Color(0xe64a19)
        val ORANGE_800: Color = Color(0xd84315)
        val ORANGE_900: Color = Color(0xbf360c)
        val ORANGE_A100: Color = Color(0xff9e80)
        val ORANGE_A200: Color = Color(0xff6e40)
        val ORANGE_A400: Color = Color(0xff3d00)
        val ORANGE_A700: Color = Color(0xdd2c00)


        operator fun Color.Companion.invoke(value: Int): Color {
            val r = value.ushr(16).and(0xFF).toFloat() / 255f
            val g = value.ushr(8).and(0xFF).toFloat() / 255f
            val b = value.ushr(0).and(0xFF).toFloat() / 255f
            return Color(r, g, b, 1f)
        }
    }
}