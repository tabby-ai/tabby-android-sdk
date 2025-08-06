package ai.tabby.android.ui.dynamic

import ai.tabby.android.data.Currency
import ai.tabby.android.data.Lang
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.factory.TabbyFactory
import ai.tabby.android.internal.network.widgetsUrl
import ai.tabby.android.ui.dynamic.interfaces.TabbySnippetJavaScriptInterface
import ai.tabby.android.ui.dynamic.model.DimensionsDto
import ai.tabby.android.ui.dynamic.model.InitializationDataDto
import ai.tabby.android.ui.dynamic.model.OpenUrlDto
import ai.tabby.android.ui.dynamic.webview.ComposeWebViewWidget
import ai.tabby.android.ui.dynamic.webview.DialogWithWebView
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebMessage
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import kotlinx.serialization.json.Json

private const val SNIPPET_WIDGET_API: String = "tabby-promo.html"

@Composable
fun TabbySnippet(
    tabbyPayment: TabbyPayment,
    modifier: Modifier = Modifier,
    dialogModifier: Modifier = Modifier,
    minHeight: Dp = 50.dp,
    lang: Lang = Lang.EN,
) {
    tabbyPayment
    var showDialog: Boolean by remember { mutableStateOf(false) }
    var openUrl: OpenUrlDto? by remember { mutableStateOf(null) }
    var dimensions: DimensionsDto? by remember { mutableStateOf(null) }

    val minWidth by remember {
        derivedStateOf {
            dimensions?.width?.dp ?: 0.dp
        }
    }
    val height by remember {
        derivedStateOf {
            dimensions?.height?.takeIf { it > 0 }?.dp ?: minHeight
        }
    }
    val snippetWidgetUrl = remember {
        TabbyFactory.tabbyComponent.getEnv().widgetsUrl + SNIPPET_WIDGET_API
    }

    ComposeWebViewWidget(
        webViewClient = object : WebViewClient() {},
        webChromeClient = object : WebChromeClient() {},
        url = snippetWidgetUrl.withParams(
            price = tabbyPayment.amount.intValueExact(),
            currency = tabbyPayment.currency,
            lang = lang,
        ),
        modifier = modifier
            .height(height)
            .heightIn(min = minHeight)
            .widthIn(min = minWidth),
        javaScriptInterface = TabbySnippetJavaScriptInterface({
            openUrl = it
            showDialog = true
        }, {
            dimensions = it
        })
    )

    SnippetDialog(
        modifier = dialogModifier,
        openUrlDto = openUrl,
        showDialog = showDialog,
        onDismissRequest = { showDialog = false }
    )
}

@Composable
private fun SnippetDialog(
    modifier: Modifier,
    openUrlDto: OpenUrlDto?,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (!showDialog) return
    openUrlDto ?: return

    DialogWithWebView(
        modifier = modifier,
        url = openUrlDto.url,
        webViewClient = object : WebViewClient() {
            @SuppressLint("NewApi")
            override fun onPageStarted(
                view: WebView,
                url: String?,
                favicon: Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                view.postWebMessage(
                    WebMessage(
                        Json.encodeToString(
                            InitializationDataDto.serializer(),
                            InitializationDataDto(data = openUrlDto.data)
                        )
                    ),
                    "*".toUri(),
                )
            }
        },
        webChromeClient = object : WebChromeClient() {},
        onDismissRequest = onDismissRequest
    )
}

private fun String.withParams(
    price: Int,
    currency: Currency,
    lang: Lang,
): String {
    return this +
            "?price=$price" +
            "&currency=${currency.name}" +
            "&lang=${lang.name.lowercase()}" +
            "&publicKey=${TabbyFactory.tabbyComponent.apiKey()}"
}

