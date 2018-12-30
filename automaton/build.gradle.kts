plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.10")
    application
}

repositories {
    jcenter()
}

val lwjglVersion = "3.2.1"
val lwjglNatives = "natives-windows"

dependencies {
    implementation(project(":keact"))
    implementation(project(":bus"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.lwjgl", "lwjgl", lwjglVersion)
    implementation("org.lwjgl", "lwjgl-assimp", lwjglVersion)
    implementation("org.lwjgl", "lwjgl-glfw", lwjglVersion)
    implementation("org.lwjgl", "lwjgl-openal", lwjglVersion)
    implementation("org.lwjgl", "lwjgl-opengl", lwjglVersion)
    implementation("org.lwjgl", "lwjgl-stb", lwjglVersion)
    runtimeOnly("org.lwjgl", "lwjgl", lwjglVersion, classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", lwjglVersion, classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", lwjglVersion, classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", lwjglVersion, classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", lwjglVersion, classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", lwjglVersion, classifier = lwjglNatives)
}

application {
    mainClassName = "automaton.AppKt"
}

