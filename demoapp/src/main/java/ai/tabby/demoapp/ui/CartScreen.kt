package ai.tabby.demoapp.ui

import ai.tabby.android.data.TabbyPayment
import ai.tabby.demoapp.R
import ai.tabby.demoapp.createSuccessfulPayment
import ai.tabby.demoapp.ui.theme.TabbyAppTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CartScreen(
    tabbyPayment: TabbyPayment,
    onCheckout: () -> Unit
) {
    TabbyAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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


@Preview(name = "Light mode",
    showBackground = true
)
@Preview(name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun CartPreview() {
    CartScreen(createSuccessfulPayment()) {}
}
