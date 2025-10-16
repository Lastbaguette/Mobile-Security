plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 31
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
        languageVersion = "2.0"
        apiVersion = "2.0"
    }

    buildFeatures {
        compose = true
    }
}

val roomVersion = "2.8.0"

dependencies {
    // Définir la nomenclature (BOM) pour gérer les versions de Compose
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))

    // Dépendances Room
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // SQL
    implementation("net.zetetic:android-database-sqlcipher:4.5.0")
    implementation("androidx.sqlite:sqlite:2.2.0")

    // Dépendances Compose (gérées par la BOM)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3") // Remplacer material par material3
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2") // Version plus récente
    implementation("androidx.activity:activity-compose:1.9.0") // Version plus récente
    implementation("androidx.appcompat:appcompat:1.7.0") // Version plus récente

    // Retrofit and OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Pdf rendering
    implementation("androidx.pdf:pdf-viewer-fragment:1.0.0-alpha10")

    // Activity KTX
    implementation("androidx.activity:activity-ktx:1.9.0") // Version plus récente
}


kapt {
    correctErrorTypes = true
}
