plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.projet1diiage.home.api"
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
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))

    implementation(project(":features:home:domain"))
    implementation(project(":features:home:data"))
    implementation(project(":features:home:ui"))

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.bundles.koin)
    implementation(libs.androidx.room.ktx)
}
