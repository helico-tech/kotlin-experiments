import org.jetbrains.kotlin.gradle.dsl.JsModuleKind
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.kotlin.wrappers.bom))
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(compose.html.svg)

                implementation("org.jetbrains.kotlin-wrappers:kotlin-browser")
            }
        }
    }
}

tasks.withType<KotlinJsCompile>().configureEach {
    compilerOptions {
        moduleKind.set(JsModuleKind.MODULE_ES)
        useEsClasses.set(true)
    }
}

