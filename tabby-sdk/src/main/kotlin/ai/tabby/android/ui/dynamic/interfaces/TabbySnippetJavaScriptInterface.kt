package ai.tabby.android.ui.dynamic.interfaces

import ai.tabby.android.ui.dynamic.model.DimensionsDto
import ai.tabby.android.ui.dynamic.model.OpenUrlDto
import android.webkit.JavascriptInterface
import kotlinx.serialization.json.Json

internal class TabbySnippetJavaScriptInterface(
    private val openUrl: (OpenUrlDto) -> Unit,
    private val setDimensions: (DimensionsDto) -> Unit,
) : JavaScriptInterface {
    override val name: String = "tabbyMobileSDK"

    @JavascriptInterface
    fun onLearnMoreClicked(msg: String) {
        val message = Json.decodeFromString(OpenUrlDto.serializer(), msg)
        openUrl(message)
    }

    @JavascriptInterface
    fun onChangeDimensions(msg: String) {
        val message = Json.decodeFromString(DimensionsDto.serializer(), msg)
        setDimensions(message)
    }
}