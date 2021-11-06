package ai.tabby.testapp

import ai.tabby.android.data.Product
import ai.tabby.android.data.tabbyResult
import ai.tabby.testapp.ui.CheckoutResultScreen
import ai.tabby.testapp.ui.InitialScreen
import ai.tabby.testapp.ui.ProductScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.screenStateFlow.collectAsState()
            when (state.value.state) {
                ScreenState.State.INITIAL -> InitialScreen(viewModel = viewModel)
                ScreenState.State.PRODUCT -> ProductScreen(viewModel = viewModel, onProductSelected = ::onProductSelected)
                ScreenState.State.CHECKOUT_RESULT -> CheckoutResultScreen(viewModel = viewModel)
            }
        }
    }

    private fun onProductSelected(product: Product) {
        val i = viewModel.createCheckoutIntent(product)
        requestForCheckout.launch(i)
    }

    private val requestForCheckout =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    result.tabbyResult?.let {
                        viewModel.onCheckoutResult(it)
                    } ?: Toast.makeText(this, "Tabby result is null", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Result is not OK", Toast.LENGTH_LONG).show()
                }
            }
        }
}


@Preview(name = "Light mode",
    showBackground = true
)
//@Preview(name = "Dark mode",
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true
//)
@Composable
fun DefaultPreview() {
    InitialScreen(viewModel = MainViewModel())
}
