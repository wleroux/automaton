package com.github.wleroux.automaton.component.window

import com.github.wleroux.keact.api.Component
import com.github.wleroux.keact.api.Node
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

class WindowComponent: Component<Unit, List<Node<*, *>>>() {
    override lateinit var properties: List<Node<*, *>>
    override var state: Unit = Unit

    private var window: Long = 0L
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

        x = 0
        y = 0
        width = 1920 / 2
        height = 1080 / 2

        window = glfwCreateWindow(width, height, "Automaton", MemoryUtil.NULL, MemoryUtil.NULL)
        require(window != MemoryUtil.NULL) { "Failed to create the GLFW window" }

        glfwSetWindowSizeCallback(window, this::windowSizeCallback)
        glfwSetKeyCallback(window) { window, key, _, action, _ ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true)
        }

        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*
            glfwGetWindowSize(window, pWidth, pHeight)

            val mode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
            glfwSetWindowPos(
                    window,
                    (mode.width() - pWidth.get(0)) / 2,
                    (mode.height() - pHeight.get(0)) / 2
            )
        }

        glfwMakeContextCurrent(window)
        glfwSwapInterval(1)
        glfwShowWindow(window)
        GL.createCapabilities()

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
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

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
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

    private fun windowSizeCallback(window: Long, width: Int, height: Int) {
        this.width = width
        this.height = height
    }

}