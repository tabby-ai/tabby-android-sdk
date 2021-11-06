package ai.tabby.testapp.ui

import ai.tabby.android.data.Product
import ai.tabby.android.data.ProductType
import ai.tabby.android.data.Session
import ai.tabby.testapp.MainViewModel
import ai.tabby.testapp.ui.theme.TabbyAppTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductScreen(
    viewModel: MainViewModel,
    onProductSelected: (Product) -> Unit
) {
    val state = viewModel.screenStateFlow.collectAsState()
    TabbyAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.value.session?.availableProducts?.forEach {
                    ProductButton(product = it) {
                        onProductSelected(it)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ProductButton(product: Product, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(
            text = when (product.type) {
                ProductType.INSTALLMENTS -> "installments"
                ProductType.PAY_LATER -> "pay later"
            },
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
fun ProductPreview() {
    ProductScreen(viewModel = MainViewModel().putDemoData()) {}
}

fun MainViewModel.putDemoData(): MainViewModel {
    onSessionSucceeded(
        Session(
            id = "xxxx",
            availableProducts = listOf(
                Product(
                    ProductType.INSTALLMENTS,
                    "https://installments.example.com"
                ),
                Product(
                    ProductType.PAY_LATER,
                    "https://paylater.example.com"
                )
            )
        )
    )
    return this
}
