package com.clean.architecture.demo.common

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


@Composable
fun AuthenticationDialog(
    activity: FragmentActivity,
    title: String = "",
    subTitle: String = "",
    description: String = "",
    onSuccess: OnResult<Unit>,
    onUserCancelled: OnResult<Unit>? = null,
    onFailure: OnResult<String>? = null,
    onAuthUnavailable: OnResult<Unit>? = null
) {

    val authenticator = BiometricManager.Authenticators.BIOMETRIC_WEAK or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL
    //check if auth is available
    val canAuth = BiometricManager.from(activity).canAuthenticate(authenticator)
    if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) {
        onAuthUnavailable?.invoke(Unit)
        return
    }

    val biometricPrompt = remember {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                    onUserCancelled?.invoke(Unit)
                }
                onFailure?.invoke("$errorCode: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess(Unit)
            }
        }
        BiometricPrompt(activity, executor, callback)
    }



    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setDescription(description)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_WEAK
                        or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()
    }

    DisposableEffect(biometricPrompt) {
        onDispose {
            biometricPrompt.cancelAuthentication()
        }
    }

    LaunchedEffect(Unit) {
        biometricPrompt.authenticate(promptInfo)
    }

}
