plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.projet1diiage.home.domain"
    compileSdk = 36

    defaultConfig {
        minSdk = 29
        targetSdk = 36
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    implementation(project(":core:domain"))

    // Coroutines pour Flow
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
