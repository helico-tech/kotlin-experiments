plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(21)

    dependencies {
        testImplementation(kotlin("test"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}