package com.github.wleroux.automaton.component.text.font

import com.github.wleroux.automaton.loadText
import java.util.regex.Pattern


object BitmapFontReader {
    fun read(file: String): Font {
        lateinit var fontInfo: Font.FontInfo
        lateinit var fontCommon: Font.FontCommon
        val fontPages = mutableMapOf<Int, String>()
        val fontCharacters = mutableMapOf<Int, Font.FontCharacter>()
        val fontKernings = mutableMapOf<Pair<Int, Int>, Int>()
        loadText(file).lines().forEach { line ->
            val typeMatcher = TYPE_PATTERN.matcher(line)
            if (typeMatcher.find()) {
                val type = typeMatcher.group(1)
                val remaining = line.substring(type.length + 1)
                when (type) {
                    "info" -> fontInfo = fontInfo(remaining)
                    "common" -> fontCommon = fontCommon(remaining)
                    "page" -> fontPages += fontPage(remaining)
                    "char" -> fontCharacters += fontCharacter(remaining)
                    "kerning" -> fontKernings += fontKerning(remaining)
                }
            }
        }

        return Font(
                fontInfo,
                fontCommon,
                fontPages,
                fontCharacters,
                fontKernings
        )
    }

    private val TYPE_PATTERN = Pattern.compile("^([a-zA-Z]+)")
    private val KEY_VALUE_PATTERN = Pattern.compile("([a-zA-Z]+)=((?:(?:[-+]?[0-9]+),?)+|\"[^\"]+\")")

    private fun fontInfo(line: String): Font.FontInfo {
        var face = ""
        var size = 0
        var bold = false
        var italic = false
        var charset = ""
        var unicode = false
        var stretchH = 0
        var smooth = false
        var aa = false
        var paddingLeft = 0
        var paddingUp = 0
        var paddingRight = 0
        var paddingDown = 0
        var spacingVertical = 0
        var spacingHorizontal = 0
        val keyValueMatcher = KEY_VALUE_PATTERN.matcher(line)
        while (keyValueMatcher.find()) {
            val key = keyValueMatcher.group(1)
            val value = keyValueMatcher.group(2)
            when (key) {
                "face" -> face = stringValue(value)
                "size" -> size = intValue(value)
                "bold" -> bold = booleanValue(value)
                "italic" -> italic = booleanValue(value)
                "charset" -> charset = stringValue(value)
                "unicode" -> unicode = booleanValue(value)
                "stretchH" -> stretchH = intValue(value)
                "smooth" -> smooth = booleanValue(value)
                "aa" -> aa = booleanValue(value)
                "padding" -> {
                    val padding = value.split(",")
                    paddingUp = intValue(padding[0])
                    paddingRight = intValue(padding[1])
                    paddingDown = intValue(padding[2])
                    paddingLeft = intValue(padding[3])
                }
                "spacing" -> {
                    val spacing = value.split(",")
                    spacingHorizontal = intValue(spacing[0])
                    spacingVertical = intValue(spacing[1])
                }
            }
        }
        return Font.FontInfo(face, size, bold, italic, charset, unicode, stretchH, smooth, aa, paddingLeft, paddingUp, paddingRight, paddingDown, spacingVertical, spacingHorizontal)
    }

    private fun fontCommon(line: String): Font.FontCommon {
        var lineHeight = 0
        var base = 0
        var scaleW = 0
        var scaleH = 0
        var pages = 1
        var packed = false

        val keyValueMatcher = KEY_VALUE_PATTERN.matcher(line)
        while (keyValueMatcher.find()) {
            val key = keyValueMatcher.group(1)
            val value = keyValueMatcher.group(2)
            when (key) {
                "lineHeight" -> lineHeight = intValue(value)
                "base" -> base = intValue(value)
                "scaleW" -> scaleW = intValue(value)
                "scaleH" -> scaleH = intValue(value)
                "pages" -> pages = intValue(value)
                "packed" -> packed = booleanValue(value)
            }
        }

        return Font.FontCommon(lineHeight, base, scaleW, scaleH, pages, packed)
    }

    private fun fontPage(line: String): Pair<Int, String> {
        var id = 0
        var file = ""
        val keyValueMatcher = KEY_VALUE_PATTERN.matcher(line)
        while (keyValueMatcher.find()) {
            val key = keyValueMatcher.group(1)
            val value = keyValueMatcher.group(2)
            when (key) {
                "id" -> id = intValue(value)
                "file" -> file = stringValue(value)
            }
        }

        return id to file
    }

    private fun fontCharacter(line: String): Pair<Int, Font.FontCharacter> {
        var id = 0
        var x = 0
        var y = 0
        var width = 0
        var height = 0
        var xOffset = 0
        var yOffset = 0
        var xAdvance = 0
        var page = 0
        var channel = 0

        val keyValueMatcher = KEY_VALUE_PATTERN.matcher(line)
        while (keyValueMatcher.find()) {
            val key = keyValueMatcher.group(1)
            val value = keyValueMatcher.group(2)
            when (key) {
                "id" -> id = intValue(value)
                "x" -> x = intValue(value)
                "y" -> y = intValue(value)
                "width" -> width = intValue(value)
                "height" -> height = intValue(value)
                "xoffset" -> xOffset = intValue(value)
                "yoffset" -> yOffset = intValue(value)
                "xadvance" -> xAdvance = intValue(value)
                "page" -> page = intValue(value)
                "chnl" -> channel = intValue(value)
            }
        }

        return id to Font.FontCharacter(x, y, width, height, xOffset, yOffset, xAdvance, page, channel)
    }

    private fun fontKerning(line: String): Pair<Pair<Int, Int>, Int> {
        var first = 0
        var second = 0
        var amount = 0

        val keyValueMatcher = KEY_VALUE_PATTERN.matcher(line)
        while (keyValueMatcher.find()) {
            val key = keyValueMatcher.group(1)
            val value = keyValueMatcher.group(2)
            when (key) {
                "first" -> first = intValue(value)
                "second" -> second = intValue(value)
                "amount" -> amount = intValue(value)
            }
        }

        return (first to second) to amount
    }

    private fun intValue(value: String?): Int {
        return checkNotNull(value).toInt()
    }
    private fun booleanValue(value: String?): Boolean {
        return checkNotNull(value).toBoolean()
    }

    private fun stringValue(value: String?): String {
        return checkNotNull(value).substring(1, value.length - 1)
    }
}