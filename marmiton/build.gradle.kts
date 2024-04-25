plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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
        kotlinCompilerExtensionVersion = "1.3.2"
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
    api("ai.mealz.core:mealzcore")
    api("ai.mealz.android:sdk")
    api(libs.android.material)

    api(libs.compose.compiler)
    api(libs.compose.ui.tooling)
    api(libs.compose.ui.core)
    api(libs.compose.foundation)
    api(libs.compose.material.core)

    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.coil.compose)
    api(libs.coil.svg)

    api(libs.androidx.navigation.runtime.ktx)
    api(libs.androidx.navigation.compose)

    api(libs.moko.resources.core)
    api(libs.moko.resources.android)

    api(libs.android.exoplayer)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}