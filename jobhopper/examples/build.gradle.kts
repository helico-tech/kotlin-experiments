plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":jobhopper:core"))
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlin.coroutines.test)
}