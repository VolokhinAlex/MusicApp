plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.volokhinaleksey.core"

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(project(Modules.models))
    implementation(Jetpack.datastore)
    implementation(Compose.composeUIUtil)
    implementation(Android.ktxCore)
    implementation(Compose.composeNavigation)
    implementation(platform(Compose.composeBom))
    implementation(Compose.composeUI)
    implementation(Compose.composeUiGraphics)
    implementation(Compose.composeCoil)
    implementation(Compose.composePreview)
    implementation(Compose.composeMaterial)
    implementation(ExoPlayer.exoPlayerUI)
    implementation(ExoPlayer.exoPlayer)
    implementation(ExoPlayer.exoPlayerSession)
    implementation(Koin.koinCore)
    implementation(Koin.koinAndroid)
    implementation(Koin.koinAndroidCompat)
    implementation(ExoPlayer.exoPlayerDash)
    testImplementation(Tests.junit)
    androidTestImplementation(Tests.extJunit)
    androidTestImplementation(Tests.espresso)
}