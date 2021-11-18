package ai.tabby.testapp

import ai.tabby.testapp.ui.CartScreen
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class CartActivity : ComponentActivity() {

    private val payment = createSuccessfulPayment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartScreen(payment) { onCheckoutClicked() }
        }
    }

    private fun onCheckoutClicked() {
        val i = Intent(this, CheckoutActivity::class.java).apply {
            putExtra(CheckoutActivity.ARG_TABBY_PAYMENT, payment)
        }
        startActivity(i)
    }
}

