plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.volokhinaleksey.home_screen"

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
    implementation(Jetpack.datastore)
    implementation(Android.ktxCore)
    implementation(Compose.composeLiveData)
    implementation(Compose.composeViewModel)
    implementation(platform(Compose.composeBom))
    implementation(Compose.composePagerLayouts)
    implementation(Compose.composeUIUtil)
    implementation(Compose.composeUI)
    implementation(Compose.composeCoil)
    implementation(Compose.composeUiGraphics)
    implementation(Compose.composeNavigation)
    implementation(Compose.composePreview)
    implementation(Compose.composeMaterial)
    implementation(Koin.koinCore)
    implementation(Koin.koinAndroid)
    implementation(Koin.koinAndroidCompat)
    implementation(Koin.koinCompose)
    implementation(ExoPlayer.exoPlayer)
    implementation(ExoPlayer.exoPlayerDash)
    implementation(ExoPlayer.exoPlayerUI)
    testImplementation(Tests.junit)
    androidTestImplementation(Tests.extJunit)
    androidTestImplementation(Tests.espresso)
}