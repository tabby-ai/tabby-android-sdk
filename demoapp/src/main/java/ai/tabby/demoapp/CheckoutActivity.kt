package ai.tabby.demoapp

import ai.tabby.android.data.Lang
import ai.tabby.android.data.Product
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.data.tabbyResult
import ai.tabby.demoapp.ui.CheckoutResultScreen
import ai.tabby.demoapp.ui.FailedScreen
import ai.tabby.demoapp.ui.ProductScreen
import ai.tabby.demoapp.ui.ProgressScreen
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/**
 * Reference implementation of a checkout screen. This is the shape a merchant's own checkout
 * activity would take: create a [ai.tabby.android.data.TabbySession] for the payment, let the
 * customer pick a product, launch [ai.tabby.android.core.Tabby.createCheckoutIntent] for result,
 * and handle the returned [ai.tabby.android.data.TabbyResult]. See README "Getting Started".
 */
class CheckoutActivity : ComponentActivity() {

    companion object {
        const val ARG_TABBY_PAYMENT = "arg.tabbyPayment"
        const val ARG_MERCHANT_CODE = "arg.merchantCode"
        const val ARG_LANG = "arg.lang"
    }

    private val viewModel: CheckoutViewModel by viewModels()

    private val tabbyPayment: TabbyPayment by lazy {
        intent.getParcelableExtra<TabbyPayment>(ARG_TABBY_PAYMENT)
            ?: throw IllegalArgumentException("Argument $ARG_TABBY_PAYMENT is missing")
    }

    private val merchantCode: String by lazy {
        intent.getStringExtra(ARG_MERCHANT_CODE) ?: "ae"
    }

    private val lang: Lang by lazy {
        intent.getStringExtra(ARG_LANG)?.let { runCatching { Lang.valueOf(it) }.getOrNull() } ?: Lang.EN
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create tabby session
        viewModel.createSession(tabbyPayment, merchantCode, lang)

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

                    ScreenState.State.SESSION_FAILED -> FailedScreen(
                        message = state.value.errorMessage,
                    ) {
                        // Retry create session
                        viewModel.createSession(tabbyPayment, merchantCode, lang)
                    }

                    ScreenState.State.CHECKOUT_RESULT -> CheckoutResultScreen(
                        viewModel = viewModel
                    ) { finish() }
                }
            }
        }
    }

    private fun onProductSelected(product: Product) {
        lifecycleScope.launch {
            val i = viewModel.createCheckoutIntent(product)
            checkoutContract.launch(i)
        }
    }

    private val checkoutContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    result.tabbyResult?.let { tabbyResult ->
                        viewModel.onCheckoutResult(tabbyResult)
                    } ?: viewModel.onCheckoutError("Tabby result is null")
                }

                else -> {
                    viewModel.onCheckoutError("Activity result was not OK (resultCode=${result.resultCode})")
                }
            }
        }

}
