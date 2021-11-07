package ai.tabby.android.internal.ui.screen

import ai.tabby.android.data.TabbyResult
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
internal fun CheckoutWebScreen(
    url: String,
    onResult: (TabbyResult) -> Unit
) {
    AndroidView(
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

                setOnKeyListener { _, _, keyEvent ->
                    if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK && !canGoBack()) {
                        false
                    } else if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == MotionEvent.ACTION_UP) {
                        goBack()
                        true
                    } else true
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
        when(msg) {
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

//private val webChromeClient = object : WebChromeClient() {
//    override fun onShowFileChooser(
//        webView: WebView?,
//        filePathCallback: ValueCallback<Array<Uri>>?,
//        fileChooserParams: FileChooserParams?
//    ): Boolean {
//        uploadMessageCallback = filePathCallback
//        openImageChooser()
//
//        return true
//    }
//
//    private fun openImageChooser() {
//        val i = Intent(Intent.ACTION_GET_CONTENT)
//        i.addCategory(Intent.CATEGORY_OPENABLE)
//        i.type = "image/*"
//        startActivityForResult(
//            Intent.createChooser(i, "Image Chooser"),
//            1
//        )
//    }
//}