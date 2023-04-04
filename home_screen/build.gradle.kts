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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(Modules.core))
    implementation(Android.ktxCore)
    implementation(Compose.composeViewModel)
    implementation(platform(Compose.composeBom))
    implementation(Compose.composePagerLayouts)
    implementation(Compose.composeUIUtil)
    implementation(Compose.composeUI)
    implementation(Compose.composeUiGraphics)
    implementation(Compose.composeNavigation)
    implementation(Compose.composePreview)
    implementation(Compose.composeMaterial)
    testImplementation(Tests.junit)
    androidTestImplementation(Tests.extJunit)
    androidTestImplementation(Tests.espresso)
}