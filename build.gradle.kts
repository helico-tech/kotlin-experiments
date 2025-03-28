plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
}

allprojects {
    group = "nl.helico"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        google()
    }
}
