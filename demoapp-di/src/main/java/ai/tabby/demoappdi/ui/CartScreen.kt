package ai.tabby.demoappdi.ui

import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.ui.TabbyInstallmentsWidget
import ai.tabby.android.ui.TabbySnippetWidget
import ai.tabby.demoappdi.R
import ai.tabby.demoappdi.createSuccessfulPayment
import ai.tabby.demoappdi.ui.theme.TabbyAppTheme
import android.content.res.Configuration
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        // A surface container using the 'background' color from the theme
        Surface(color = Color.White) {
            Column(
                modifier = Modifier.fillMaxSize(),
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
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
