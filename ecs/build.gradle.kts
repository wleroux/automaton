plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.10")
}

repositories {
    jcenter()
}

dependencies {
    implementation(project(":bus"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}
