package com.clean.architecture.demo.common

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.SoftwareKeyboardController
import kotlinx.serialization.json.Json

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Modifier.clearFocusWhenTap(
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController? = null
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
            keyboardController?.hide()
        })
    }
}

fun String.removeSpaces(): String {
    return this.replace("　", " ") // make full-width space to half-width space
        .trim() // remove leading and trailing spaces
        .replace("\\s+".toRegex(), " ") // ensure only single spaces inside the string
}

