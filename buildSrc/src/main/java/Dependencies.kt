import org.gradle.api.JavaVersion

object Config {
    const val applicationId = "com.volokhinaleksey.musicapp"
    const val compileSdk = 33
    const val minSdk = 27
    const val targetSdk = 33
    val javaVersion = JavaVersion.VERSION_1_8
}

object Releases {
    const val versionCode = 1
    const val versionName = "1.0"
}

object Versions {
    // Android Ktx
    const val ktxCore = "1.9.0"

    // Lifecycle
    const val lifecycleRuntimeKtx = "2.6.1"

    // Compose
    const val activityCompose = "1.7.0"
    const val composeBom = "2022.10.00"
    const val composeViewModel = "2.5.1"
    const val composeNavigation = "2.6.0-alpha08"
    const val composePagerLayouts = "0.30.0"
    const val composePermission = "0.31.1-alpha"
    const val composeCoil = "2.3.0"

    // Koin
    const val koinCore = "3.3.3"
    const val androidKoin = "3.3.3"
    const val compatAndroidKoin = "3.3.3"
    const val koinTest = "3.3.3"
    const val koinCompose = "3.4.3"

    // Tests
    const val junit = "4.13.2"
    const val extJunit = "1.1.5"
    const val espresso = "3.5.1"

    // ExoPlayerMedia3
    const val exoplayer = "1.0.0"
}

object Modules {
    const val core = ":core"
    const val homeScreen = ":home_screen"
    const val descriptionScreen = ":description_screen"
    const val interactors = ":interactors"
    const val models = ":models"
    const val datasource = ":datasource"
    const val repositories = ":repositories"
}

object Android {
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktxCore}"
}

object Compose {
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"
    const val composeUI = "androidx.compose.ui:ui"
    const val composeUIUtil = "androidx.compose.ui:ui-util"
    const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    const val composePreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial = "androidx.compose.material3:material3"
    const val composeUiTestJunit = "androidx.compose.ui:ui-test-junit4"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling"
    const val composeTestManifest = "androidx.compose.ui:ui-test-manifest"
    const val composeViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.composeViewModel}"
    const val composeLiveData = "androidx.compose.runtime:runtime-livedata"
    const val composeNavigation =
        "androidx.navigation:navigation-compose:${Versions.composeNavigation}"
    const val composePagerLayouts =
        "com.google.accompanist:accompanist-pager:${Versions.composePagerLayouts}"
    const val composePermission =
        "com.google.accompanist:accompanist-permissions:${Versions.composePermission}"
    const val composeCoil = "io.coil-kt:coil-compose:${Versions.composeCoil}"
}

object ExoPlayer {
    const val exoPlayer = "androidx.media3:media3-exoplayer:${Versions.exoplayer}"
    const val exoPlayerDash =  "androidx.media3:media3-exoplayer-dash:${Versions.exoplayer}"
    const val exoPlayerUI =  "androidx.media3:media3-ui:${Versions.exoplayer}"
    const val exoPlayerSession = "androidx.media3:media3-session:${Versions.exoplayer}"
}

object Tests {

    const val junit = "junit:junit:${Versions.junit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val extJunit = "androidx.test.ext:junit:${Versions.extJunit}"
}

object Koin {
    const val koinCore = "io.insert-koin:koin-core:${Versions.koinCore}"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.androidKoin}"
    const val koinAndroidCompat = "io.insert-koin:koin-android-compat:${Versions.compatAndroidKoin}"
    const val koinTest = "io.insert-koin:koin-test:${Versions.koinTest}"
    const val koinCompose = "io.insert-koin:koin-androidx-compose:${Versions.koinCompose}"
}

object Lifecycle {
    const val lifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"
}