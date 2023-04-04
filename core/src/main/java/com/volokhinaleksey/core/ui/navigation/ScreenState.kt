package com.volokhinaleksey.core.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.volokhinaleksey.core.R

/**
 * The class is needed for easy navigation between screens
 * @param route - Needed to determine the screen for navigation
 * @param label - Needed to display the screen name in the bottom navigation bar. The value as String Res
 * @param icon - Needed to display the screen icon in the bottom navigation bar. The value as Drawable Res
 */

sealed class ScreenState(
    val route: String,
    @StringRes val label: Int,
    val icon: ImageVector?
) {
    object HomeScreen :
        ScreenState(route = "home_screen", label = R.string.home_screen, icon = Icons.Outlined.Home)

    object DescriptionMusicScreen : ScreenState(
        route = "description_screen",
        label = R.string.description_screen,
        icon = null
    )
}
