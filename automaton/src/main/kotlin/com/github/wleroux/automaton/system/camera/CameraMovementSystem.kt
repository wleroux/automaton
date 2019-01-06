package com.github.wleroux.automaton.system.camera

import com.github.wleroux.automaton.System
import com.github.wleroux.automaton.common.math.Vector2f
import com.github.wleroux.automaton.common.math.Vector3f
import com.github.wleroux.automaton.component.launcher.game.event.GameTickedEvent
import com.github.wleroux.automaton.component.window.event.Keys
import com.github.wleroux.automaton.data.type.CAMERA
import com.github.wleroux.automaton.data.type.KEYBOARD
import com.github.wleroux.automaton.data.type.MOUSE
import com.github.wleroux.bus.api.BusSubscription
import com.github.wleroux.bus.api.message.MessageHandlerBuilder.Companion.messageHandler
import com.github.wleroux.bus.api.message.event.DefaultEventHandlerBuilder.Companion.eventHandler
import com.github.wleroux.ecs.api.Game
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

class CameraMovementSystem: System {
    private lateinit var subscription: BusSubscription
    override fun initialize(game: Game) {
        subscription = game.subscribe(messageHandler {
            +eventHandler { event: GameTickedEvent ->
                val keyboard = game[KEYBOARD]
                val mouse = game[MOUSE]
                val dt = event.dt.toFloat() / TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS).toFloat()

                // Zoom
                game[CAMERA].zoomLevel = min(max(10f, game[CAMERA].zoomLevel - mouse.yoffset.toFloat() * dt * 250f), 100f)
                mouse.yoffset = 0.0

                // Rotation
                val dr = when {
                    keyboard.isPressed(Keys.Q) && keyboard.isPressed(Keys.E) -> 0f
                    keyboard.isPressed(Keys.Q) -> -1f
                    keyboard.isPressed(Keys.E) -> 1f
                    else -> 0f
                }

                if (dr != 0f) {
                    game[CAMERA].rotationAngle += dr * dt
                }

                // Translation
                val movementRotation = game[CAMERA].rotation.copy(x = 0f, z = 0f).normalize()
                val dx = when {
                    keyboard.isPressed(Keys.A) && keyboard.isPressed(Keys.D) -> 0f
                    keyboard.isPressed(Keys.A) -> -1f
                    keyboard.isPressed(Keys.D) -> 1f
                    else -> 0f
                }
                val dz = when {
                    keyboard.isPressed(Keys.S) && keyboard.isPressed(Keys.W) -> 0f
                    keyboard.isPressed(Keys.S) -> -1f
                    keyboard.isPressed(Keys.W) -> 1f
                    else -> 0f
                }

                if (dx != 0f || dz != 0f) {
                    val dv = Vector3f(dx, 0f, dz) * movementRotation * dt * 25f * (game[CAMERA].zoomLevel / 25f)
                    game[CAMERA].center += Vector2f(dv.x, dv.z)
                }
            }
        })
    }

    override fun terminate() {
        subscription.close()
    }
}