package ai.tabby.android.internal.ui

import ai.tabby.android.data.TabbyResult
import ai.tabby.android.internal.ui.screen.TestCheckoutScreen
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

internal class CheckoutActivity : ComponentActivity() {

    companion object {
        const val EXTRA_TABBY_RESULT = "extra.tabbyResult"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestCheckoutScreen(::onResult)
        }
    }

    private fun onResult(result: TabbyResult) {
        val i = Intent().apply {
            putExtra(EXTRA_TABBY_RESULT, result)
        }
        setResult(RESULT_OK, i)
        finish()
    }
}
