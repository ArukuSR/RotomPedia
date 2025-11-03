// build.gradle.kts (PROYECTO - raíz)
plugins {
    id("com.android.application") version "8.12.3" apply false
    // Sincronizado a 2.0.21
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false

    // Plugins que usaremos en :app
    id("com.google.devtools.ksp") version "2.0.21-1.0.25" apply false
    // Sincronizado a 2.0.21
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}