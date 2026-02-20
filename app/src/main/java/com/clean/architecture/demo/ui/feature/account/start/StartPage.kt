package com.clean.architecture.demo.ui.feature.account.start

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clean.architecture.demo.R
import com.clean.architecture.demo.app.Page
import com.clean.architecture.demo.app.PageNavigator
import com.clean.architecture.demo.common.OnClick
import com.clean.architecture.demo.common.UiEvent
import com.clean.architecture.demo.common.collectWithLifecycle
import com.clean.architecture.demo.common.toast
import com.clean.architecture.demo.domain.model.StartPageState
import com.clean.architecture.demo.ui.theme.MyDemoTheme
import com.clean.architecture.demo.ui.widgets.AppAlertDialog
import com.clean.architecture.demo.ui.widgets.CardDialog
import com.clean.architecture.demo.ui.widgets.ConfirmButton
import com.clean.architecture.demo.ui.widgets.SpacerHeight
import com.clean.architecture.demo.ui.widgets.SystemVerifyDialog
import com.clean.architecture.demo.ui.widgets.WebWidget
import com.clean.architecture.demo.ui.widgets.cardPadding
import kotlinx.coroutines.delay
import kotlin.system.exitProcess


@Composable
fun StartPage(
    navigator: PageNavigator,
    viewModel: StartViewModel = hiltViewModel()
) {
    var started by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current
    val errorCodeMsg = stringResource(R.string.invalid_code)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.uiEvent.collectWithLifecycle { event ->
        if(event is UiEvent.Toast) {
            val msg = if(event.msg == "invalid code") errorCodeMsg else event.msg
            context.toast(msg)
        }
    }

    LaunchedEffect(uiState.pageState) {
        when (uiState.pageState) {
            is StartPageState.Login -> navigator.replaceWith(Page.Login)
            is StartPageState.Verify -> navigator.replaceWith(Page.Verify)
            else -> Unit
        }
    }

    Crossfade(
        targetState = started,
        label = "app_entry"
    ) { showSplash ->
        if(showSplash) {
            SplashLayout { started = false }
        } else {
            when(uiState.pageState) {
                is StartPageState.FirstIn -> FirstIn {
                    viewModel.showAgreementDialog()
                }
                is StartPageState.Agreement -> AgreementLayout(
                    agreementUrl = viewModel.getAgreementUrl(),
                    agreed = { viewModel.checkAgreement() },
                    disagreed = { viewModel.disagreeAgreement() },
                )
                is StartPageState.SystemVerification -> {
                    val activity = LocalActivity.current
                    SystemVerifyDialog(
                        context = context,
                        cancelled = { // kill process
                            activity?.finishAffinity()
                            exitProcess(0)
                        },
                        success = { navigator.clearThenGoTo(Page.Home) }
                    )
                }
                else -> {}
            }

            if(uiState.agreementDisagreed) {
                AppAlertDialog(stringResource(R.string.agreement_hint)) {
                    viewModel.resetStartState()
                }
            }
        }
    }

}

@Composable
private fun SplashLayout(onSplashFinished: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(1600)
        onSplashFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.page_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}


@Composable
fun AgreementLayout(
    modifier: Modifier = Modifier,
    agreementUrl: String,
    disagreed: OnClick,
    agreed: OnClick
)
{
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CardDialog(
            modifier = modifier.padding(horizontal = cardPadding),
            content = {
                Column(modifier = modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.agreement),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    SpacerHeight(10)
                    Text(
                        text = stringResource(R.string.start_agreement_text),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    SpacerHeight(20)
                    WebWidget(
                        url = agreementUrl,
                        modifier = modifier.fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = 1.dp,
                                color = colorResource(R.color.content_bg),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        textSizePercent = 100,
                    )
                }
            },
            bottom = {
                Row(
                    modifier = modifier.fillMaxSize().padding(horizontal = cardPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.disagree),
                        fontSize = 14.sp,
                        modifier = modifier.clickable(
                            onClick = { disagreed() }
                        ),
                    )
                    Text(
                        text = stringResource(R.string.agree_next),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier.clickable(
                            onClick = { agreed() }
                        ),
                    )
                }
            }
        )
    }
}


@Composable
fun FirstIn(start: OnClick) {
    BoxWithConstraints (
        modifier = Modifier.fillMaxSize(),
    ) {

        val phoneOffsetY = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            phoneOffsetY.animateTo(
                targetValue = 30f,
                animationSpec = tween(
                    durationMillis = 3000,
                    easing = LinearOutSlowInEasing
                )
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center)
                .offset(y = (-20).dp)
                .width(maxWidth * 0.75f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_start_slogan),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ),
                fontSize = 13.sp,
                lineHeight = 20.sp
            )

            SpacerHeight(30)
            ConfirmButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                text = stringResource(R.string.app_start),
                minHeight = 40.dp,
                roundCornerSize = 4,
                alpha = 0.9f,
                fontSize = 13.sp,
                onClick = start
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun StartPagePreview() {
    MyDemoTheme(darkTheme = false) {
//        FirstIn {  }

        AgreementLayout(
            agreementUrl = "",
            disagreed = {},
            agreed = {}
        )
    }

}


