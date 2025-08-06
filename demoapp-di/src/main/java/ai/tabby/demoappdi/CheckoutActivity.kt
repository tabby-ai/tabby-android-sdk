package ai.tabby.demoappdi

import ai.tabby.android.data.Product
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.data.tabbyResult
import ai.tabby.demoappdi.ui.CheckoutResultScreen
import ai.tabby.demoappdi.ui.FailedScreen
import ai.tabby.demoappdi.ui.ProductScreen
import ai.tabby.demoappdi.ui.ProgressScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

class CheckoutActivity : ComponentActivity() {

    companion object {
        const val ARG_TABBY_PAYMENT = "arg.tabbyPayment"
    }

    private val viewModel: CheckoutViewModel by viewModels { CheckoutVMFactory(application) }

    private val tabbyPayment: TabbyPayment by lazy {
        intent.getParcelableExtra<TabbyPayment>(ARG_TABBY_PAYMENT)
            ?: throw IllegalArgumentException("Argument $ARG_TABBY_PAYMENT is missing")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create tabby session
        viewModel.createSession(tabbyPayment)

        setContent {
            val state = viewModel.screenStateFlow.collectAsState()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.safeDrawing.asPaddingValues())
            ) {
                when (state.value.state) {
                    ScreenState.State.INITIAL -> {}
                    ScreenState.State.CREATING_SESSION -> ProgressScreen(
                        tabbyPayment = tabbyPayment
                    )

                    ScreenState.State.SESSION_CREATED -> ProductScreen(
                        viewModel = viewModel,
                        tabbyPayment = tabbyPayment,
                        onProductSelected = ::onProductSelected
                    )

                    ScreenState.State.SESSION_FAILED -> FailedScreen {
                        // Retry create session
                        viewModel.createSession(tabbyPayment = tabbyPayment)
                    }

                    ScreenState.State.CHECKOUT_RESULT -> CheckoutResultScreen(
                        viewModel = viewModel
                    ) { finish() }
                }
            }
        }
    }

    private fun onProductSelected(product: Product) {
        val i = viewModel.createCheckoutIntent(product)
        checkoutContract.launch(i)
    }

    private val checkoutContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    result.tabbyResult?.let { tabbyResult ->
                        viewModel.onCheckoutResult(tabbyResult)
                    } ?: Toast.makeText(this, "Tabby result is null", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, "Result is not OK", Toast.LENGTH_LONG).show()
                }
            }
        }

}