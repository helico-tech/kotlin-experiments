plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(17)

    dependencies {
        implementation(compose.runtime)
        testImplementation(kotlin("test"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
