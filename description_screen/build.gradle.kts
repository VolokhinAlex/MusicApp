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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(Modules.core))
    implementation(Android.ktxCore)
    implementation(Compose.composeNavigation)
    implementation(Compose.composeViewModel)
    implementation(Compose.composeLiveData)
    implementation(platform(Compose.composeBom))
    implementation(Compose.composeUI)
    implementation(Compose.composeUiGraphics)
    implementation(Compose.composePreview)
    implementation(Compose.composeMaterial)
    testImplementation(Tests.junit)
    androidTestImplementation(Tests.extJunit)
    androidTestImplementation(Tests.espresso)
}