package ai.tabby.android.internal.ui

import ai.tabby.android.data.TabbyResult
import ai.tabby.android.internal.ui.screen.CheckoutWebScreen
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

internal class CheckoutActivity : ComponentActivity() {

    companion object {
        const val EXTRA_WEB_URL = "extra.webUrl"
        const val EXTRA_TABBY_RESULT = "extra.tabbyResult"
    }

    private val webUrl: String by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra(EXTRA_WEB_URL)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            CheckoutTestScreen(::onResult)
//            CheckoutWebScreen("http://exif-viewer.com/", webChromeClient, ::onResult)
            CheckoutWebScreen(webUrl, webChromeClient, ::onResult)
        }
    }

    private fun onResult(result: TabbyResult) {
        val i = Intent().apply {
            putExtra(EXTRA_TABBY_RESULT, result)
        }
        setResult(RESULT_OK, i)
        finish()
    }

    override fun onBackPressed() {
        onResult(TabbyResult(TabbyResult.Result.CLOSED))
    }

    private var uploadMessageCallback: ValueCallback<Array<Uri>>? = null

    private val webChromeClient = object : WebChromeClient() {

        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            uploadMessageCallback = filePathCallback
            openImageChooser()
            return true
        }

        private fun openImageChooser() {
            val i = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            fileChooserContract.launch(i)
        }
    }

    private val fileChooserContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val callback = uploadMessageCallback ?: return@registerForActivityResult

            val uriList = if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val dataString = data.dataString
                    val clipData = data.clipData
                    when {
                        dataString != null -> listOf(Uri.parse(dataString))
                        clipData != null -> (0 until clipData.itemCount).map { count ->
                            clipData.getItemAt(count).uri
                        }
                        else -> listOf()
                    }
                } else {
                    // data is null
                    listOf()
                }
            } else {
                // result is not ok
                listOf()
            }
            callback.onReceiveValue(uriList.toTypedArray())
            uploadMessageCallback = null
        }
}
