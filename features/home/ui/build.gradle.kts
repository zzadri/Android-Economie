plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.projet1diiage.home.ui"
    compileSdk = 36
    defaultConfig { minSdk = 29 /* pas de targetSdk pour un module lib */ }
    buildFeatures { compose = true }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_11; targetCompatibility = JavaVersion.VERSION_11 }
    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":features:home:domain"))

    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Lifecycle & activity
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Navigation compose (utilisé par l'app, utile si prévisualisations naviguent)
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // DI Koin (Compose)
    implementation(platform("io.insert-koin:koin-bom:4.1.1"))
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-androidx-compose:4.1.1") // Ajout pour koinViewModel

    // icones

    implementation("androidx.compose.material:material-icons-extended")
}