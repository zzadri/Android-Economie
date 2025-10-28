plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.projet1diiage"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.projet1diiage"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.navigation:navigation-compose:2.8.0")

    implementation(platform("androidx.compose:compose-bom"))
    implementation("androidx.compose.material3:material3")

    implementation("com.google.android.material:material:1.12.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.bundles.compose.ui)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(project(":core:domain"))
    implementation(project(":core:ui"))

    implementation(project(":features:home:ui"))
    implementation(project(":features:home:api"))

    implementation(project(":features:profile:ui"))
    implementation(project(":features:profile:api"))

    // Icons extended for bottom navigation icons
    implementation("androidx.compose.material:material-icons-extended")

    debugImplementation(libs.androidx.compose.ui.tooling)
}
