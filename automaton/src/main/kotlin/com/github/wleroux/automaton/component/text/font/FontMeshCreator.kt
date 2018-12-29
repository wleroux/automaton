package com.github.wleroux.automaton.component.text.font

import com.github.wleroux.automaton.program.Mesh
import org.lwjgl.opengl.GL11.GL_FLOAT

object FontMeshCreator {
    fun generateTextMesh(text: String, font: Font, size: Int): Mesh {
        val vertices = mutableListOf<Float>()
        val indices = mutableListOf<Int>()
        val sizeFactor = size.toFloat() / font.info.size.toFloat()

        var indexCount = 0
        var cursorX = 0f
        val cursorY = 0f
        var previousCharacterId: Int? = null
        text.chars().forEach { characterId ->
            val character = font.characters[characterId] ?: return@forEach

            // Kerning support
            previousCharacterId?.let {
                cursorX += (font.kerning[previousCharacterId to characterId] ?: 0) * sizeFactor
            }

            val characterX = cursorX + (character.xOffset.toFloat() * sizeFactor)
            val characterY = cursorY + (character.yOffset.toFloat() * sizeFactor)
            val characterWidth = character.width.toFloat() * sizeFactor
            val characterHeight = character.height.toFloat() * sizeFactor

            vertices += listOf(
                    characterX,
                    characterY,
                    character.x.toFloat() / font.common.scaleW.toFloat(),
                    (font.common.scaleH.toFloat() - character.y.toFloat()) / font.common.scaleH.toFloat()
            )
            vertices += listOf(
                    characterX + characterWidth,
                    characterY,
                    (character.x.toFloat() + character.width.toFloat()) / font.common.scaleW.toFloat(),
                    (font.common.scaleH.toFloat() - character.y.toFloat()) / font.common.scaleH.toFloat()
            )
            vertices += listOf(
                    characterX + characterWidth,
                    characterY + characterHeight,
                    (character.x.toFloat() + character.width.toFloat()) / font.common.scaleW.toFloat(),
                    (font.common.scaleH.toFloat() - character.y.toFloat() - character.height.toFloat()) / font.common.scaleH.toFloat()
            )
            vertices += listOf(
                    characterX,
                    characterY + characterHeight,
                    character.x.toFloat() / font.common.scaleW.toFloat(),
                    (font.common.scaleH.toFloat() - character.y.toFloat() - character.height.toFloat()) / font.common.scaleH.toFloat()
            )
            indices += listOf(indexCount + 0, indexCount + 1, indexCount + 2)
            indices += listOf(indexCount + 2, indexCount + 3, indexCount + 0)
            indexCount += 4

            cursorX += character.xAdvance * sizeFactor
            previousCharacterId = characterId
        }

        return Mesh(vertices.toTypedArray(), indices.toTypedArray(), listOf(
                Mesh.Attribute("position", GL_FLOAT, 2),
                Mesh.Attribute("texCoord", GL_FLOAT, 2)
        ))
    }

    fun getWidth(text: String, font: Font, size: Int): Float {
        val sizeFactor = size.toFloat() / font.info.size.toFloat()
        var cursorX = 0f
        text.chars().forEach { char ->
            val character = font.characters[char] ?: return@forEach
            cursorX += character.xAdvance * sizeFactor
        }
        return cursorX
    }

    fun getHeight(text: String, font: Font, size: Int): Float {
        val sizeFactor = size.toFloat() / font.info.size.toFloat()
        return font.common.lineHeight.toFloat() * sizeFactor
    }

    fun getCursorPosition(x: Int, y: Int, text: String, font: Font, size: Int): Int {
        val sizeFactor = size.toFloat() / font.info.size.toFloat()
        var cursorX = 0f
        text.toCharArray().forEachIndexed { cursorPosition, char ->
            val character = font.characters[char.toInt()] ?: return@forEachIndexed
            val characterWidth = character.xAdvance * sizeFactor
            when {
                (cursorX + characterWidth / 2) > x -> return cursorPosition - 1
                (cursorX + characterWidth) > x -> return cursorPosition
                else -> cursorX += characterWidth
            }
        }
        return text.length
    }
}