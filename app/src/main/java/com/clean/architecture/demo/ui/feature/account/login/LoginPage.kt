package com.clean.architecture.demo.ui.feature.account.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clean.architecture.demo.R
import com.clean.architecture.demo.app.Page
import com.clean.architecture.demo.app.PageNavigator
import com.clean.architecture.demo.common.State
import com.clean.architecture.demo.common.clearFocusWhenTap
import com.clean.architecture.demo.common.toastLong
import com.clean.architecture.demo.ui.component.AppAlertDialog
import com.clean.architecture.demo.ui.component.DelayLoadingDialog
import com.clean.architecture.demo.ui.component.CardDialog
import com.clean.architecture.demo.ui.component.AppTextField
import com.clean.architecture.demo.ui.component.SpacerHeight
import com.clean.architecture.demo.ui.component.SystemVerifyDialog
import com.clean.architecture.demo.ui.component.cardPadding

@Composable
fun LoginPage(
    navigator: PageNavigator,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginPageLayout(
        modifier.fillMaxSize().padding(16.dp),
        navigator,
        uiState,
        viewModel
    )

    DelayLoadingDialog(uiState.loginState == State.Loading)

    when(uiState.loginState) {
        is State.Success<*> -> {
            if(uiState.require2FA) {
                uiState.loginState.data()?.let { context.toastLong(it) }
                navigator.goTo(Page.Verify)
            } else {
                SystemVerifyDialog(context) { navigator.clearThenGoTo(Page.Home) }
            }
            viewModel.resetLoginState()
        }
        is State.Failure -> {
            val msg = uiState.loginState.failureMsg() ?: return
            AppAlertDialog(title = stringResource(R.string.confirm), message = msg) {
                viewModel.resetLoginState()
            }
        }
        else -> {}
    }

}

@Composable
fun LoginPageLayout(
    modifier: Modifier,
    navigator: PageNavigator,
    uiState: LoginUiState,
    loginPageEvent: LoginPageEvent
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
            .clearFocusWhenTap(focusManager),
        contentAlignment = Alignment.Center
    ) {
        CardDialog(
            modifier = Modifier.padding(horizontal = cardPadding),
            content = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.login),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                        ),
                    )

                    SpacerHeight(20)

                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .background(
                                color = colorResource(R.color.content_bg),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        AppTextField(
                            label = stringResource(R.string.email_address),
                            text = uiState.email
                        )
                        {
                            loginPageEvent.onEmailChanged(it)
                        }

                        AppTextField(
                            label = stringResource(R.string.password),
                            text = uiState.password,
                            keyboardType = KeyboardType.Password,
                        ) {
                            loginPageEvent.onPasswordChanged(it)
                        }

                    }

                    SpacerHeight(15)

                    Text(
                        text = stringResource(R.string.new_register),
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(bottom = 12.dp, end = 2.dp)
                            .align(Alignment.End)
                            .clickable(onClick = { navigator.goTo(Page.Register) })
                    )


                }
            },
            bottom = {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .clickable(
                            enabled = uiState.loginEnabled(),
                            onClick = { loginPageEvent.login() }
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    val textColor = if(uiState.loginEnabled()) {
                        colorResource(R.color.text_black)
                    } else {
                        colorResource(R.color.color_unable)
                    }
                    Text(
                        text = stringResource(R.string.login),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 16.sp,
                            color = textColor
                        ),
                    )
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    LoginPageLayout(
        modifier = Modifier,
        navigator = PageNavigator.previewNavigator(),
        uiState = LoginUiState(),
        loginPageEvent = object : LoginPageEvent{
            override fun login() {}
            override fun onEmailChanged(email: String) {}
            override fun onPasswordChanged(password: String) {}
        })
}