package com.clean.architecture.demo.ui.feature.account.register

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clean.architecture.demo.R
import com.clean.architecture.demo.app.PageNavigator
import com.clean.architecture.demo.common.OnClick
import com.clean.architecture.demo.common.State
import com.clean.architecture.demo.common.UiEvent
import com.clean.architecture.demo.common.clearFocusWhenTap
import com.clean.architecture.demo.common.collectWithLifecycle
import com.clean.architecture.demo.ui.theme.MyDemoTheme
import com.clean.architecture.demo.ui.component.AppAlertDialog
import com.clean.architecture.demo.ui.component.AppTextField
import com.clean.architecture.demo.ui.component.BackButton
import com.clean.architecture.demo.ui.component.CardDialog
import com.clean.architecture.demo.ui.component.SpacerHeight
import com.clean.architecture.demo.ui.component.cardPadding

@Composable
fun RegisterPage(
    navigator: PageNavigator,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.uiEvent.collectWithLifecycle {
        if (it is UiEvent.Navigate) {
            navigator.goTo(it.targetPage)
        }
    }

    if(uiState.registerState is State.Failure) {
        uiState.registerState.failureMsg()?.let {
            AppAlertDialog(it) { viewModel.resetRegisterState() }
        }
    }

    RegisterPageContent(uiState, viewModel, modifier) {
        navigator.goBack()
    }

}

@Composable
fun RegisterPageContent(
    uiState: RegisterUiState,
    event: RegisterEvent,
    modifier: Modifier,
    onBackClicked: OnClick
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize().clearFocusWhenTap(focusManager),
        contentAlignment = Alignment.Center
    ) {
        BackButton(
            modifier = Modifier.align(Alignment.TopStart)
                .padding(start = 8.dp, top = 20.dp),
            onClick = onBackClicked,
        )

        CardDialog(
            modifier = Modifier.padding(horizontal = cardPadding),
            content = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.user_register),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp
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
                            label = stringResource(R.string.name),
                            text = uiState.name
                        )
                        {
                            event.onNameInputted(it)
                        }

                        AppTextField(
                            label = stringResource(R.string.name_kana),
                            text = uiState.kana
                        ) {
                            event.onKanaNameInputted(it)
                        }

                        AppTextField(
                            label = stringResource(R.string.email_address),
                            text = uiState.email,
                        ) {
                            event.onEmailInputted(it)
                        }

                        AppTextField(
                            label = stringResource(R.string.password),
                            keyboardType = KeyboardType.Password,
                            text = uiState.password,
                        ) {
                            event.onPasswordInputted(it)
                        }

                        AppTextField(
                            label = stringResource(R.string.password_repeat),
                            keyboardType = KeyboardType.Password,
                            text = uiState.passwordConfirm,
                        ) {
                            event.onPasswordConfirmInputted(it)
                        }
                    }
                }
            },
            bottom = {
                Box(
                    modifier = Modifier.fillMaxSize().clickable(
                        enabled = uiState.registerEnable(),
                        onClick = { event.register() }
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    val textColor = if(uiState.registerEnable()) {
                        colorResource(R.color.text_black)
                    } else {
                        colorResource(R.color.color_unable)
                    }
                    Text(
                        text = stringResource(R.string.start_app),
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

@Composable
@Preview(showBackground = true)
fun RegisterPagePreview() {
    MyDemoTheme(darkTheme = false) {
        RegisterPageContent(
            uiState = RegisterUiState(),
            modifier = Modifier,
            event = object : RegisterEvent {
                override fun register() {}
                override fun onNameInputted(name: String) {}
                override fun onKanaNameInputted(kana: String) {}
                override fun onEmailInputted(email: String) {}
                override fun onPasswordInputted(password: String) {}
                override fun onPasswordConfirmInputted(passwordConfirm: String) {}
                override fun resetRegisterState() {}
            },
            onBackClicked = {}
        )
    }

}
