// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // KSP y Compose Compiler se definieron en la raíz, aquí solo se aplican.
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.duoc.rotompedia"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.duoc.rotompedia"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Retrofit + Moshi
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    // Generador de código Moshi (KSP)
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    // Coil para Compose (Carga de Imágenes)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // --- DEPENDENCIAS DE COMPOSE (AQUÍ ESTÁ LA MAGIA) ---

    // La BOM (Bill of Materials) asegura que todas las librerías de Compose
    // que uses tengan versiones compatibles entre sí.
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics") // Es buena práctica añadir esta también
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Testing Básico
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // 1. Añade la BOM también para las pruebas de instrumentación.
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))

    // 2. Añade la librería para pruebas de UI con Compose.
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // 3. Dependencias de "debug" para herramientas y el manifest de pruebas.
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}