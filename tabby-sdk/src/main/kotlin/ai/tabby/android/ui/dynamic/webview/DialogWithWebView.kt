package ai.tabby.android.ui.dynamic.webview

import ai.tabby.android.ui.dynamic.interfaces.JavaScriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
internal fun DialogWithWebView(
    webViewClient: WebViewClient,
    webChromeClient: WebChromeClient,
    url: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    javaScriptInterface: JavaScriptInterface? = null,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            decorFitsSystemWindows = false,
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier,
        ) {
            ComposeWebViewWidget(
                webViewClient = webViewClient,
                webChromeClient = webChromeClient,
                url = url,
                modifier = Modifier.fillMaxSize(),
                javaScriptInterface = javaScriptInterface,
            )
        }
    }
}