package com.clean.architecture.demo.app

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * All the pages in the app are defined here.
 */

@Serializable
sealed interface Page: NavKey {
    @Serializable data object Start : Page
    @Serializable data object Login : Page
    @Serializable data object Register : Page
    @Serializable data object Verify : Page
    @Serializable data object Home : Page

}

