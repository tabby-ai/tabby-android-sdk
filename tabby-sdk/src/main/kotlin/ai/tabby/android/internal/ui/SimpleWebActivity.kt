package ai.tabby.android.internal.ui

import ai.tabby.android.R
import ai.tabby.android.internal.analytics.api.segmentAnalytics
import ai.tabby.android.internal.ui.analutics.LearnMorePopupClosed
import ai.tabby.android.internal.ui.analutics.LearnMorePopupOpened
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity

internal class SimpleWebActivity : ComponentActivity() {

    private val initialUrl: String
        get() = requireNotNull(
            intent.getStringExtra(EXTRA_URL)
        )
    private val installmentsCount: Int
        get() = intent.getIntExtra(EXTRA_INSTALLMENT_COUNT, 0)
    private val currencyName: String
        get() = requireNotNull(
            intent.getStringExtra(EXTRA_CURRENCY)
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_web_activity)

        findViewById<WebView>(R.id.webView).apply {
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            settings.domStorageEnabled = true

            webViewClient = WebViewClient()

            setOnKeyListener { _, _, keyEvent ->
                if (keyEvent.action == MotionEvent.ACTION_UP &&
                    keyEvent.keyCode == KeyEvent.KEYCODE_BACK &&
                    canGoBack()
                ) {
                    goBack()
                }
                keyEvent.keyCode != KeyEvent.KEYCODE_BACK || canGoBack()
            }

            loadUrl(initialUrl)
        }

        segmentAnalytics.sendEvent(LearnMorePopupOpened(installmentsCount, currencyName))
    }

    override fun onDestroy() {
        super.onDestroy()
        segmentAnalytics.sendEvent(LearnMorePopupClosed(installmentsCount, currencyName))
    }

    companion object {
        fun createIntent(
            context: Context,
            url: String,
            installmentsCount: Int,
            currencyName: String
        ) = Intent(context, SimpleWebActivity::class.java).apply {
            putExtra(EXTRA_URL, url)
            putExtra(EXTRA_INSTALLMENT_COUNT, installmentsCount)
            putExtra(EXTRA_CURRENCY, currencyName)
        }

        private const val EXTRA_URL = "extra.Url"
        private const val EXTRA_INSTALLMENT_COUNT = "extra.InstallmentCount"
        private const val EXTRA_CURRENCY = "extra.Currency"
    }
}

