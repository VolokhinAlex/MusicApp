package com.volokhinaleksey.core.ui.navigation

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable


/**
 * Extension to fix the deprecated getParcelable method, which became deprecated with API 33
 *
 * If the API of the application is lower than 33, then the usual getParcelable with the key will be used.
 * If the API of the application is equal to or higher than 33,then a constructor with a key and a
 * java class (Generic <T>) for which data is obtained will be used
 */

const val DATA_KEY = "DATA_KEY"

inline fun <reified T : Parcelable> Bundle.parcelable(): T? = when {
    SDK_INT >= 33 -> getParcelable(DATA_KEY, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(DATA_KEY) as? T
}