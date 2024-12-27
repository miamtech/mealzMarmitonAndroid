plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

android {
    namespace = "ai.mealz.marmiton"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api("ai.mealz.android:mealz-android:5.0.0")
    api(libs.android.material)
    api(libs.ktor.client.serialization)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.datetime)
    api(libs.ktor.client.core)
    api(libs.ktor.client.encoding)
    api(libs.ktor.client.serialization)
    api(libs.ktor.client.contentNegotiation)
    api(libs.ktor.client.logging)
    api(libs.koin.core)
    api(libs.kotlinx.datetime)
    api(libs.ktor.client.android)
    api(libs.compose.compiler)
    api(libs.compose.ui.tooling)
    api(libs.compose.ui.core)
    api(libs.compose.foundation)
    api(libs.compose.material.core)
    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.coil.compose)
    api(libs.coil.svg)
    api(libs.play.services.location)

    api(libs.androidx.navigation.runtime.ktx)
    api(libs.androidx.navigation.compose)

    api(libs.moko.resources.core)
    api(libs.moko.resources.android)

    api(libs.android.exoplayer)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "ia.mealz.marmiton"
            artifactId = "mealz-marmiton"
            version = "4.1.0-alpha"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}