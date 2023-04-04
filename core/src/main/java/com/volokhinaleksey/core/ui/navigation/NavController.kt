package com.volokhinaleksey.core.ui.navigation

import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * Extensions for the NavController class, for convenient data transfer between screens using bundle
 * Thanks to this, any parcelable objects can be transferred between screens
 * @param route - The topmost destination to retain
 * @param args - Arguments to be passed between screens
 * @param navOptions - special options for this navigation operation
 * @param navigatorExtras - extras to pass to the Navigator
 */

fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(route).toUri())
        .build()
    val deepLinkMatch = graph.matchDeepLink(routeLink)
    if (deepLinkMatch != null) {
        val destination = deepLinkMatch.destination
        navigate(destination.id, args, navOptions, navigatorExtras)
    } else {
        navigate(route, navOptions, navigatorExtras)
    }
}