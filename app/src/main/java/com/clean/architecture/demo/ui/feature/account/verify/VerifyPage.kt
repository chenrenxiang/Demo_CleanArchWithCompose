package com.clean.architecture.demo.ui.feature.account.verify

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clean.architecture.demo.R
import com.clean.architecture.demo.app.Page
import com.clean.architecture.demo.app.PageNavigator
import com.clean.architecture.demo.common.OnClick
import com.clean.architecture.demo.common.OnResult
import com.clean.architecture.demo.common.UiEvent
import com.clean.architecture.demo.common.clearFocusWhenTap
import com.clean.architecture.demo.common.collectWithLifecycle
import com.clean.architecture.demo.common.toastLong
import com.clean.architecture.demo.ui.theme.MyDemoTheme
import com.clean.architecture.demo.ui.component.AppAlertDialog
import com.clean.architecture.demo.ui.component.BackButton
import com.clean.architecture.demo.ui.component.CodeInputWidget
import com.clean.architecture.demo.ui.component.CardDialog
import com.clean.architecture.demo.ui.component.SpacerHeight
import com.clean.architecture.demo.ui.component.SpacerWidth
import com.clean.architecture.demo.ui.component.SystemVerifyDialog
import com.clean.architecture.demo.ui.component.cardPadding

@Composable
fun VerifyPage(
    navigator: PageNavigator,
    modifier: Modifier = Modifier,
    viewModel: VerifyViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.uiEvent.collectWithLifecycle {
        if(it is UiEvent.Toast) {
            context.toastLong(it.msg)
        }
    }

    val errorMsg = uiState.verifyState.failureMsg()
    if(!errorMsg.isNullOrBlank()) {
        AppAlertDialog(errorMsg) { viewModel.resetVerifyState() }
    }

    if (uiState.verifyState.isSuccess()) {
        if(uiState.useBiometricAuth) {
            SystemVerifyDialog(context) {
                navigator.clearThenGoTo(Page.Home)
            }
        } else {
            navigator.clearThenGoTo(Page.Home)
        }
    }

    VerifyLayout(
        onBackClicked = { navigator.goBack() },
        onNextClicked = {
            viewModel.verify(it.first, it.second)
        }
    )

}

@Composable
private fun VerifyLayout(
    onBackClicked: OnClick,
    onNextClicked: OnResult<Pair<String, Boolean>>
) {
    var inputtedCode by remember { mutableStateOf("") }
    var useBiometricAuth by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
            .imePadding()
            .clearFocusWhenTap(focusManager),
        contentAlignment = Alignment.Center
    ) {
        BackButton(
            modifier = Modifier.align(Alignment.TopStart)
                .padding(start = 8.dp, top = 20.dp),
            onClick = onBackClicked
        )

        CardDialog(
            modifier = Modifier.padding(horizontal = cardPadding),
            content = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.code_verify),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    SpacerHeight(10)

                    Text(
                        text = buildAnnotatedString {
                            append("メールに記載されている")
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)){
                                append("6桁の認証コード")
                            }
                            append("をご入力いただき、次へをタップします。")
                        },
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    SpacerHeight(20)
                    CodeInputWidget(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        length = 6,
                        space = 4.dp,
                        width = 305.dp,
                        height = 95.dp
                    )
                    {
                        inputtedCode = it
                    }
                    SpacerHeight(20)
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "今後ログインに生体認証を利用",
                            fontSize = 12.sp,
                        )

                        SpacerWidth(8)

                        Switch(
                            modifier = Modifier.scale(scale = 0.8f),
                            colors = SwitchDefaults.colors(
                                uncheckedBorderColor = Color.Transparent,
                                checkedBorderColor = Color.Transparent,
                                checkedThumbColor = Color.White,
                                uncheckedThumbColor = Color.White,
                            ),
                            checked = useBiometricAuth,
                            onCheckedChange = { checked ->
                                useBiometricAuth = checked
                            },
                        )
                    }
                }
            },
            bottom = {
                Box(
                    modifier = Modifier.fillMaxSize().clickable(
                        enabled = inputtedCode.length == 6,
                        onClick = {
                            onNextClicked(inputtedCode to useBiometricAuth)
                        }
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    val textColor = if(inputtedCode.length == 6) {
                        colorResource(R.color.text_black)
                    } else {
                        colorResource(R.color.color_unable)
                    }
                    Text(
                        text = stringResource(R.string.next),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }

            }
        )
    }



}

@Composable
@Preview(showBackground = true)
fun VerifyLayoutPreview() {
    MyDemoTheme(darkTheme = false) {
        VerifyLayout(onBackClicked = {}, onNextClicked = {})
    }
}
