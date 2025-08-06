package ai.tabby.android.internal.ui

import ai.tabby.android.data.TabbyResult
import ai.tabby.android.internal.permissions.PermissionRequester
import ai.tabby.android.internal.permissions.WebViewPermissions
import ai.tabby.android.internal.ui.screen.CheckoutWebScreen
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

internal class TabbyCheckoutActivity : ComponentActivity() {

    companion object {
        const val EXTRA_WEB_URL = "extra.webUrl"
        const val EXTRA_TABBY_RESULT = "extra.tabbyResult"
    }

    private val webUrl: String by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra(EXTRA_WEB_URL)!!
    }

    private val permissionRequester: PermissionRequester = PermissionRequester()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        permissionRequester
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionRequester.activityResultLauncher = permissionLauncher
        setContent {
//            CheckoutTestScreen(::onResult)
//            CheckoutWebScreen("http://exif-viewer.com/", webChromeClient, ::onResult)
            CheckoutWebScreen(
                url = webUrl,
                webChromeClient = webChromeClient,
                modifier = Modifier.padding(WindowInsets.safeDrawing.asPaddingValues()),
                onResult = ::onResult
            )
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

        override fun onPermissionRequest(request: PermissionRequest) {
            lifecycleScope.launch {
                val result = permissionRequester.requestPermissions(
                    request.resources.mapNotNull {
                        WebViewPermissions.byPermission(it)
                    }
                )
                val granted = result.filter { it.isGranted == true }

                if (granted.isNotEmpty()) {
                    request.grant(granted.map { it.permission.permission }
                        .toTypedArray())
                } else {
                    request.deny()
                }
            }
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
