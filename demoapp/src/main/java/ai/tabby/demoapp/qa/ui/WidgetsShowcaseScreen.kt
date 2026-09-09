package ai.tabby.demoapp.qa.ui

import ai.tabby.android.data.Lang
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.ui.TabbyInstallmentsWidget
import ai.tabby.android.ui.TabbySnippetWidget
import ai.tabby.android.ui.dynamic.TabbyCardSnippetView
import ai.tabby.android.ui.dynamic.TabbySnippetView
import ai.tabby.demoapp.qa.DemoConfig
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import android.view.ViewGroup

/**
 * Exercises every standalone Tabby widget bound to the payment currently configured on the
 * payment builder screen, so QA can visually verify each one without leaving the app.
 */
@Composable
fun WidgetsShowcaseScreen(payment: TabbyPayment) {
    val merchantCode = DemoConfig.merchantCode
    val lang = DemoConfig.lang

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        WidgetCard(
            title = "TabbyInstallmentsWidget",
            snippet = """
                TabbyInstallmentsWidget(context).apply {
                    amount = payment.amount
                    currency = payment.currency
                }
            """.trimIndent(),
        ) {
            TabbyInstallmentsWidgetComposable(tabbyPayment = payment)
        }

        WidgetCard(
            title = "TabbySnippetWidget",
            snippet = """
                TabbySnippetWidget(context).apply {
                    amount = payment.amount
                    currency = payment.currency
                }
            """.trimIndent(),
        ) {
            TabbySnippetWidgetComposable(tabbyPayment = payment)
        }

        WidgetCard(
            title = "TabbySnippetView",
            snippet = """
                TabbySnippetView(context).apply {
                    tabbyPayment = payment
                }
            """.trimIndent(),
        ) {
            TabbySnippetComposable(tabbyPayment = payment)
        }

        WidgetCard(
            title = "TabbyCardSnippetView / TabbyCardSnippet",
            snippet = """
                TabbyCardSnippetView(context).apply {
                    tabbyPayment = payment
                    merchantCode = "$merchantCode"
                    lang = Lang.${lang.name}
                }
            """.trimIndent(),
        ) {
            TabbyCardSnippetComposable(tabbyPayment = payment, merchantCode = merchantCode, lang = lang)
        }
    }
}

@Composable
private fun WidgetCard(title: String, snippet: String, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.05f),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(snippet, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
            Spacer(Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun TabbyInstallmentsWidgetComposable(tabbyPayment: TabbyPayment) {
    AndroidView(
        factory = { context -> TabbyInstallmentsWidget(context) },
        update = { widget ->
            widget.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            widget.amount = tabbyPayment.amount
            widget.currency = tabbyPayment.currency
        }
    )
}

@Composable
private fun TabbySnippetWidgetComposable(tabbyPayment: TabbyPayment) {
    AndroidView(
        factory = { context -> TabbySnippetWidget(context) },
        update = { widget ->
            widget.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            widget.amount = tabbyPayment.amount
            widget.currency = tabbyPayment.currency
        }
    )
}

@Composable
private fun TabbySnippetComposable(tabbyPayment: TabbyPayment) {
    AndroidView(
        factory = { context -> TabbySnippetView(context) },
        update = { widget ->
            widget.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            widget.tabbyPayment = tabbyPayment
        }
    )
}

@Composable
private fun TabbyCardSnippetComposable(tabbyPayment: TabbyPayment, merchantCode: String, lang: Lang) {
    AndroidView(
        factory = { context -> TabbyCardSnippetView(context) },
        update = { widget ->
            widget.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            widget.tabbyPayment = tabbyPayment
            widget.merchantCode = merchantCode
            widget.lang = lang
        }
    )
}
