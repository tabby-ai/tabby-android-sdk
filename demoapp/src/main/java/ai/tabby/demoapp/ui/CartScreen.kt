package ai.tabby.demoapp.ui

import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.ui.TabbyInstallmentsWidget
import ai.tabby.android.ui.TabbySnippetWidget
import ai.tabby.demoapp.R
import ai.tabby.demoapp.createSuccessfulPayment
import ai.tabby.demoapp.ui.theme.TabbyAppTheme
import android.content.res.Configuration
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CartScreen(
    tabbyPayment: TabbyPayment,
    onCheckout: () -> Unit
) {
    TabbyAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())
        ) {
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TabbyInstallmentsWidgetComposable(tabbyPayment = tabbyPayment)
                    Spacer(modifier = Modifier.height(18.dp))
                    TabbySnippetWidgetComposable(tabbyPayment = tabbyPayment)
                    Spacer(modifier = Modifier.height(18.dp))
                    CartWidget(tabbyPayment = tabbyPayment)
                    Spacer(modifier = Modifier.height(18.dp))
                    CheckoutButton(onCheckout)
                }
            }
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
fun CheckoutButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(
            text = "Checkout",
            style = MaterialTheme.typography.button,
            fontSize = 18.sp
        )
    }
}

@Composable
fun TabbyInstallmentsWidgetComposable(tabbyPayment: TabbyPayment) {
    AndroidView(
        factory = { context ->
            TabbyInstallmentsWidget(context)
        },
        update = { widget ->
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            widget.layoutParams = params
            widget.amount = tabbyPayment.amount
            widget.currency = tabbyPayment.currency
        }
    )
}

@Composable
fun TabbySnippetWidgetComposable(tabbyPayment: TabbyPayment) {
    AndroidView(
        factory = { context ->
            TabbySnippetWidget(context)
        },
        update = { widget ->
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            widget.layoutParams = params
            widget.amount = tabbyPayment.amount
            widget.currency = tabbyPayment.currency
        }
    )
}

@Preview(
    name = "Light mode",
    showBackground = true
)
@Preview(
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun CartPreview() {
    CartScreen(createSuccessfulPayment()) {}
}
