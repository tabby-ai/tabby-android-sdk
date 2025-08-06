package ai.tabby.android.ui.dynamic.webview

import ai.tabby.android.ui.dynamic.interfaces.JavaScriptInterface
import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun ComposeWebViewWidget(
    webViewClient: WebViewClient,
    webChromeClient: WebChromeClient,
    url: String,
    modifier: Modifier = Modifier,
    javaScriptInterface: JavaScriptInterface? = null,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                this.webViewClient = webViewClient
                this.webChromeClient = webChromeClient
                with(settings) {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                }

                if (javaScriptInterface != null) {
                    addJavascriptInterface(
                        javaScriptInterface,
                        javaScriptInterface.name,
                    )
                }

                loadUrl(url)
            }
        },
        update = {
            it.loadUrl(url)
        },
    )
}