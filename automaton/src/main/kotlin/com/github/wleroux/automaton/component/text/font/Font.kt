package com.github.wleroux.automaton.component.text.font


data class Font(
        val info: FontInfo,
        val common: FontCommon,
        val pages: Map<Int, String>,
        val characters: Map<Int, FontCharacter>,
        val kerning: Map<Pair<Int, Int>, Int>
) {
    data class FontInfo(
            val face: String,
            val size: Int,
            val bold: Boolean,
            val italic: Boolean,
            val charset: String,
            val unicode: Boolean,
            val stretchH: Int,
            val smooth: Boolean,
            val aa: Boolean,
            val paddingLeft: Int,
            val paddingTop: Int,
            val paddingRight: Int,
            val paddingBottom: Int,
            val spacingVertical: Int,
            val spacingHorizontal: Int
    )

    data class FontCommon(
            val lineHeight: Int,
            val base: Int,
            val scaleW: Int,
            val scaleH: Int,
            val pages: Int,
            val packed: Boolean
    )

    data class FontCharacter(
            val x: Int,
            val y: Int,
            val width: Int,
            val height: Int,
            val xOffset: Int,
            val yOffset: Int,
            val xAdvance: Int,
            val page: Int,
            val channel: Int
    )
}