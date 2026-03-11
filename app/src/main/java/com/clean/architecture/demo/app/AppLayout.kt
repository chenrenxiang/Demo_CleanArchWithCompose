package com.clean.architecture.demo.app
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import com.clean.architecture.demo.BuildConfig
import com.clean.architecture.demo.R
import com.clean.architecture.demo.common.UiEvent
import com.clean.architecture.demo.common.collectWithLifecycle
import com.clean.architecture.demo.common.toastLong
import com.clean.architecture.demo.domain.repository.NetworkStatus
import com.clean.architecture.demo.ui.theme.MyDemoTheme

@Composable
fun AppComposableLayout() {
    //page stack
    val pageStack = rememberNavBackStack(Page.Start)
    //AppViewModel
    val appViewModel: AppViewModel = hiltViewModel()
    val appUiState by appViewModel.uiState.collectAsStateWithLifecycle()
    //
    val context = LocalContext.current
    val networkUnavailableTxt = stringResource(R.string.network_disconnect)

    //show toast when network is unavailable
    LaunchedEffect(appUiState.networkStatus) {
        if(appUiState.networkStatus == NetworkStatus.Unavailable) {
            context.toastLong(networkUnavailableTxt)
        }
    }

    appViewModel.uiEvent.collectWithLifecycle {
        when(it) {
            is UiEvent.Navigate -> {
                if(it.targetPage == Page.Start) {
                    pageStack.clear()
                    pageStack.add(Page.Start)
                } else {
                    pageStack.add(it.targetPage)
                }
            }
            is UiEvent.Toast -> context.toastLong(it.msg)
        }
    }

    MyDemoTheme {
        // a single Scaffold for all pages
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            // the pages
            content = { innerPadding ->
                AppBackground {
                    AppNavDisplay(
                        pageStack = pageStack,
                        appViewModel = appViewModel,
                        modifier = Modifier.fillMaxSize().padding(
                            top = 0.dp,
                            bottom = innerPadding.calculateBottomPadding(),
                            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                        )
                    )
                }
            }
        )

    }
}


@Composable
fun AppBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(R.color.page_bg_color))
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.page_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        content()

        //add a "DEBUG" ribbon in debug mode
        if(BuildConfig.DEBUG) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(width = 100.dp, height = 14.dp)
                    .offset(x = 30.dp, y = 10.dp)
                    .graphicsLayer{
                        rotationZ = 45f
                        shadowElevation = 2.dp.toPx()
                    }
                    .background(color = colorResource(R.color.bg_ribbon)),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "DEBUG",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 8.sp,
                        color = colorResource(R.color.white)
                    ),
                )
            }
        }
    }
}


