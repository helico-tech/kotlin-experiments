plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.typesafe.config)
    implementation(libs.kafka.clients)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.coroutines.core)
}
