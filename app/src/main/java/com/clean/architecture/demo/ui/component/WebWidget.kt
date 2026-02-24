package com.clean.architecture.demo.ui.component

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebWidget(
    url: String,
    modifier: Modifier = Modifier,
    textSizePercent: Int = 100,
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {

                settings.apply {
                    javaScriptEnabled = true
                    setSupportZoom(false)
                    builtInZoomControls = false
                    displayZoomControls = false
                    textZoom = textSizePercent
                }

                webViewClient = WebViewClient()

                loadUrl(url)
            }
        },
    )
}



