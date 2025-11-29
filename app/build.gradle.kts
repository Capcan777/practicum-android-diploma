plugins {
    id("com.android.application")
    alias(libs.plugins.jetbrains.kotlin.android)
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("com.google.devtools.ksp") version "2.1.20-2.0.0" // Подключение ksp
    id("androidx.navigation.safeargs.kotlin") // Safe Args
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(type = "String", name = "API_ACCESS_TOKEN", value = "\"${developProperties.apiAccessToken}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // UI layer libraries
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)

    // region Unit tests
    testImplementation(libs.unitTests.junit)
    // endregion

    // region UI tests
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
    // endregion

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)

    // Koin (DI)
    implementation(libs.koin.android)

    // Room (Database)
    ksp(libs.room.compiler.v261) // KSP для Room
    implementation(libs.room.ktx) // Поддержка корутин
    implementation(libs.room.runtime)

    // Lifecycle
    implementation(libs.lifecycle.livedata.ktx)

    // Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.navigation.compose)

    // Coil for image loading in Compose
    implementation(libs.coil.compose)
}
