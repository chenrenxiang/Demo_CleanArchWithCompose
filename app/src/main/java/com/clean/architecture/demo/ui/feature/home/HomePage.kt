package com.clean.architecture.demo.ui.feature.home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.clean.architecture.demo.app.AppViewModel
import com.clean.architecture.demo.app.PageNavigator

@Composable
fun HomePage(
    navigator: PageNavigator,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    // Nothing here
}
