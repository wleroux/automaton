package com.github.wleroux.automaton.component.window

import com.github.wleroux.automaton.component.window.event.*
import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import com.github.wleroux.keact.api.dispatch
import com.github.wleroux.keact.api.event.Event
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil

class WindowComponent: Component<Unit, List<Node<*, *>>>() {
    override lateinit var properties: List<Node<*, *>>
    override var state: Unit = Unit

    private var window: Long = 0L
    private var focus: Component<*, *>? = null
    private var hover: Component<*, *>? = null
    private var mouseX = 0
    private var mouseY = 0
    var isActive = false

    override fun componentDidMount() {
        GLFWErrorCallback.createPrint(System.err).set()
        require(glfwInit()) { "Unable to initialize GLFW" }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        val mode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        x = 0
        y = 0
        width = (mode.width() * 2) / 3
        height = (mode.height() * 2) / 3

        window = glfwCreateWindow(width, height, "Automaton", MemoryUtil.NULL, MemoryUtil.NULL)
        require(window != MemoryUtil.NULL) { "Failed to create the GLFW window" }
        glfwSetWindowPos(window, (mode.width() - width) / 2, (mode.height() - height) / 2)

        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE)
        glfwSetCharModsCallback(window, this::charModsCallback)
        glfwSetKeyCallback(window, this::keyCallback)

        glfwSetWindowSizeCallback(window, this::windowSizeCallback)
        glfwSetWindowCloseCallback(window, this::windowCloseCallback)
        glfwSetCursorPosCallback(window, this::cursorPosCallback)
        glfwSetMouseButtonCallback(window, this::mouseButtonCallback)

        glfwMakeContextCurrent(window)
        glfwSwapInterval(1)
        glfwShowWindow(window)
        GL.createCapabilities()

        glClearColor(0.3f, 0.3f, 0.3f, 0.0f)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glDepthMask(true)
        glDepthFunc(GL_LEQUAL)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glFrontFace(GL_CW)

        isActive = true
    }

    override fun componentWillUnmount() {
        Callbacks.glfwFreeCallbacks(window)
        glfwDestroyWindow(window)

        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }

    override fun asNodes() = properties

    override fun render() {
        if (glfwWindowShouldClose(window)) {
            isActive = false
            return
        }

        GL11.glClearColor(0.1f, 0.1f, 0.1f, 0.0f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glEnable(GL11.GL_DEPTH_TEST)

        childComponents.values.forEach { childComponent ->
            childComponent.x = 0
            childComponent.y = 0
            childComponent.width = width
            childComponent.height = height
            childComponent.render()
        }

        glfwSwapBuffers(window)
        glfwPollEvents()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun windowSizeCallback(window: Long, width: Int, height: Int) {
        this.width = width
        this.height = height
    }
    private fun windowCloseCallback(window: Long) {
        glfwSetWindowShouldClose(window, true)
    }

    override fun handle(event: Event) {
        if (event.data is KeyStroke) {
            val keyStroke = event.data as KeyStroke
            if(keyStroke.action == KeyAction.RELEASED && keyStroke.character == 0x1.toChar()) {
                glfwSetWindowShouldClose(window, true)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun cursorPosCallback(window: Long, xpos: Double, ypos: Double) {
        mouseX = xpos.toInt()
        mouseY = height - ypos.toInt()

        val nextHover = findComponentAt(mouseX, mouseY)
        val prevHover = hover
        if (prevHover !== nextHover) {
            prevHover?.dispatch(MouseExit(mouseX, mouseY))
            nextHover?.dispatch(MouseEntered(mouseX, mouseY))
            hover = nextHover
        }
        hover?.dispatch(MouseMove(mouseX, mouseY))
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        if (button == GLFW_MOUSE_BUTTON_LEFT || button == GLFW_MOUSE_BUTTON_RIGHT || button == GLFW_MOUSE_BUTTON_MIDDLE) {
            findComponentAt(mouseX, mouseY)?.dispatch(MouseClick(
                    when (action) {
                        GLFW_PRESS -> MouseAction.PRESSED
                        GLFW_RELEASE -> MouseAction.RELEASED
                        else -> throw IllegalStateException()
                    },
                    when (button) {
                        GLFW_MOUSE_BUTTON_LEFT -> MouseButton.LEFT
                        GLFW_MOUSE_BUTTON_RIGHT -> MouseButton.RIGHT
                        GLFW_MOUSE_BUTTON_MIDDLE -> MouseButton.MIDDLE
                        else -> throw IllegalStateException()
                    },
                    mouseX,
                    mouseY,
                    (mods and GLFW_MOD_SHIFT) != 0,
                    (mods and GLFW_MOD_CONTROL) != 0,
                    (mods and GLFW_MOD_ALT) != 0,
                    (mods and GLFW_MOD_SUPER) != 0
            ))
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        val keyStroke = KeyStroke(
                scancode.toChar(),
                when (action) {
                    GLFW_PRESS -> KeyAction.PRESSED
                    GLFW_RELEASE -> KeyAction.RELEASED
                    else -> throw IllegalStateException()
                },
                (mods and GLFW_MOD_SHIFT) != 0,
                (mods and GLFW_MOD_CONTROL) != 0,
                (mods and GLFW_MOD_ALT) != 0,
                (mods and GLFW_MOD_SUPER) != 0
        )
        (focus ?: this).dispatch(keyStroke)
    }
    @Suppress("UNUSED_PARAMETER")
    private fun charModsCallback(window: Long, codepoint: Int, mods: Int) {
        val keyStroke = KeyStroke(
                codepoint.toChar(),
                KeyAction.TYPED,
                (mods and GLFW_MOD_SHIFT) != 0,
                (mods and GLFW_MOD_CONTROL) != 0,
                (mods and GLFW_MOD_ALT) != 0,
                (mods and GLFW_MOD_SUPER) != 0
        )
        (focus ?: this).dispatch(keyStroke)
    }
}
