package ai.tabby.android.internal.ui.screen

import ai.tabby.android.data.TabbyResult
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
internal fun CheckoutWebScreen(
    url: String,
    webChromeClient: WebChromeClient,
    onResult: (TabbyResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                settings.allowFileAccess = true
                settings.domStorageEnabled = true

                addJavascriptInterface(WebListener(onResult), "tabbyMobileSDK")

                webViewClient = WebViewClient()
                this.webChromeClient = webChromeClient

                setOnKeyListener { _, _, keyEvent ->
                    when (keyEvent.keyCode) {
                        KeyEvent.KEYCODE_BACK -> when {
                            !canGoBack() -> false
                            keyEvent.action == MotionEvent.ACTION_UP -> {
                                goBack(); true
                            }

                            else -> true
                        }

                        else -> true
                    }
                }

                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}

private class WebListener(
    val onResult: (TabbyResult) -> Unit
) {

    @JavascriptInterface
    fun postMessage(msg: String) {
        when (msg) {
            "authorized" -> {
                // Payment is authorized
                onResult(TabbyResult(TabbyResult.Result.AUTHORIZED))
            }

            "rejected" -> {
                onResult(TabbyResult(TabbyResult.Result.REJECTED))
            }

            "close" -> {
                onResult(TabbyResult(TabbyResult.Result.CLOSED))
            }

            "expired" -> {
                onResult(TabbyResult(TabbyResult.Result.EXPIRED))
            }
        }
    }
}

