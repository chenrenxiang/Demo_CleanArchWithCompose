package com.clean.architecture.demo.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.clean.architecture.demo.ui.feature.account.login.LoginPage
import com.clean.architecture.demo.ui.feature.account.register.RegisterPage
import com.clean.architecture.demo.ui.feature.account.start.StartPage
import com.clean.architecture.demo.ui.feature.account.verify.VerifyPage
import com.clean.architecture.demo.ui.feature.home.HomePage

interface INavigator {
    fun goTo(key: NavKey)
    fun goBack()
    fun clearThenGoTo(key: NavKey)
    fun replaceWith(key: NavKey)
}

class PageNavigator(private val pageStack: NavBackStack<NavKey>): INavigator {

    companion object {
        fun previewNavigator(navKey: NavKey? = null) = PageNavigator(
            if(navKey == null) NavBackStack() else NavBackStack(navKey)
        )
    }

    override fun goTo(key: NavKey) {
        pageStack.add(key)
    }

    override fun goBack() {
        pageStack.removeLastOrNull()
    }

    override fun replaceWith(key: NavKey) {
        pageStack.removeLastOrNull()
        pageStack.add(key)
    }

    override fun clearThenGoTo(key: NavKey) {
        pageStack.clear()
        pageStack.add(key)
    }

}

@Composable
fun AppNavDisplay(
    pageStack: NavBackStack<NavKey>,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val navigator = PageNavigator(pageStack)
    NavDisplay(
        modifier = modifier,
        backStack = pageStack,
        onBack = { pageStack.removeLastOrNull() },
        entryDecorators = listOf(
            //Bind rememberSaveable state to each NavEntry
            //(cleared when entry is popped, restored on recreation)
            rememberSaveableStateHolderNavEntryDecorator(),
            //Provide independent ViewModelStoreOwner for each NavEntry
            //(ViewModels are cleared when entry is removed)
            rememberViewModelStoreNavEntryDecorator()
        ),
        predictivePopTransitionSpec = { _ ->
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        entryProvider = entryProvider {
            entry<Page.Start> { StartPage(navigator) }
            entry<Page.Login> { LoginPage(navigator) }
            entry<Page.Register> { RegisterPage(navigator) }
            entry<Page.Verify> { VerifyPage(navigator) }
            entry<Page.Home> {
                HomePage(
                    navigator = navigator,
                    appViewModel = appViewModel
                )
            }
        },
    )
}
