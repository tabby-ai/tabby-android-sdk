package ai.tabby.android.internal.ui

import ai.tabby.android.data.TabbyResult
import ai.tabby.android.internal.ui.screen.CheckoutWebScreen
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

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
            CheckoutWebScreen(url = webUrl, ::onResult)
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
}
