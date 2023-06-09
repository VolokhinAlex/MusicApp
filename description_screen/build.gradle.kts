plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.volokhinaleksey.description_screen"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.models))
    implementation(project(Modules.interactors))
    implementation(Android.ktxCore)
    implementation(Jetpack.datastore)
    implementation(Compose.composeNavigation)
    implementation(Compose.composeViewModel)
    implementation(Compose.composeLiveData)
    implementation(platform(Compose.composeBom))
    implementation(Compose.composeUI)
    implementation(Compose.composeUiGraphics)
    implementation(Compose.composePreview)
    implementation(Compose.composeMaterial)
    implementation(Koin.koinCore)
    implementation(Koin.koinAndroid)
    implementation(Koin.koinAndroidCompat)
    implementation(Koin.koinCompose)
    implementation(Compose.composeCoil)
    implementation(ExoPlayer.exoPlayer)
    implementation(ExoPlayer.exoPlayerDash)
    implementation(ExoPlayer.exoPlayerUI)
    testImplementation(Tests.junit)
    androidTestImplementation(Tests.extJunit)
    androidTestImplementation(Tests.espresso)
}