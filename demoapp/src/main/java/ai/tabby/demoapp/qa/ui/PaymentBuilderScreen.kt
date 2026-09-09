package ai.tabby.demoapp.qa.ui

import ai.tabby.android.data.Buyer
import ai.tabby.android.data.Currency
import ai.tabby.android.data.Lang
import ai.tabby.android.data.Order
import ai.tabby.android.data.OrderItem
import ai.tabby.android.data.ShippingAddress
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.data.TabbyResult
import ai.tabby.android.data.tabbyResult
import ai.tabby.demoapp.CheckoutActivity
import ai.tabby.demoapp.qa.DemoConfig
import ai.tabby.demoapp.qa.PaymentPresets
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * QA home screen: pick a test-data preset or hand-edit the payment payload, then start checkout.
 * Shows the outcome of the last run (status + raw payload) without leaving the screen.
 */
@Composable
fun PaymentBuilderScreen(
    payment: TabbyPayment,
    onPaymentChange: (TabbyPayment) -> Unit,
    lastResult: TabbyResult?,
    lastError: String?,
    onCheckoutFinished: (TabbyResult?, String?) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenWidgets: () -> Unit,
) {
    val context = LocalContext.current
    var merchantCode by remember { mutableStateOf(DemoConfig.merchantCode) }
    var lang by remember { mutableStateOf(DemoConfig.lang) }
    var advancedExpanded by remember { mutableStateOf(false) }

    val checkoutLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onCheckoutFinished(result.tabbyResult, null)
        } else {
            onCheckoutFinished(null, "Activity result was not OK (resultCode=${result.resultCode})")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onOpenWidgets) { Text("Widgets") }
            TextButton(onClick = onOpenSettings) { Text("Settings") }
        }

        Text("Test data preset", style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            PaymentPresets.Preset.entries.forEach { preset ->
                OutlinedButton(onClick = { onPaymentChange(PaymentPresets.payment(preset)) }) {
                    Text(preset.label)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Divider()
        Spacer(Modifier.height(16.dp))

        Text("Payment", style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = payment.amount.toPlainString(),
            onValueChange = { text ->
                text.toBigDecimalOrNull()?.let { onPaymentChange(payment.copy(amount = it)) }
            },
            label = { Text("Amount") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        Text("Currency", style = MaterialTheme.typography.caption)
        Spacer(Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        ) {
            Currency.entries.forEach { currency ->
                SelectableChip(
                    label = currency.name,
                    selected = currency == payment.currency,
                    onClick = { onPaymentChange(payment.copy(currency = currency)) },
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = payment.description.orEmpty(),
            onValueChange = { onPaymentChange(payment.copy(description = it)) },
            label = { Text("Description") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))
        Text("Merchant", style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = merchantCode,
            onValueChange = { merchantCode = it; DemoConfig.merchantCode = it },
            label = { Text("Merchant code") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        Text("Language", style = MaterialTheme.typography.caption)
        Spacer(Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Lang.entries.forEach { langOption ->
                SelectableChip(
                    label = langOption.name,
                    selected = langOption == lang,
                    onClick = { lang = langOption; DemoConfig.lang = langOption },
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Customer", style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = payment.buyer.name,
            onValueChange = { onPaymentChange(payment.copy(buyer = payment.buyer.copy(name = it))) },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = payment.buyer.email,
            onValueChange = { onPaymentChange(payment.copy(buyer = payment.buyer.copy(email = it))) },
            label = { Text("Email — try successful.payment@tabby.ai / rejected@tabby.ai on stage") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = payment.buyer.phone,
            onValueChange = { onPaymentChange(payment.copy(buyer = payment.buyer.copy(phone = it))) },
            label = { Text("Phone") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))
        TextButton(onClick = { advancedExpanded = !advancedExpanded }) {
            Text(if (advancedExpanded) "Hide order & shipping details" else "Edit order & shipping details")
        }
        if (advancedExpanded) {
            AdvancedOrderFields(payment = payment, onPaymentChange = onPaymentChange)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                val intent = Intent(context, CheckoutActivity::class.java).apply {
                    putExtra(CheckoutActivity.ARG_TABBY_PAYMENT, payment)
                    putExtra(CheckoutActivity.ARG_MERCHANT_CODE, merchantCode)
                    putExtra(CheckoutActivity.ARG_LANG, lang.name)
                }
                checkoutLauncher.launch(intent)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Start checkout", fontSize = 16.sp)
        }

        if (lastResult != null || lastError != null) {
            Spacer(Modifier.height(24.dp))
            Divider()
            Spacer(Modifier.height(12.dp))
            Text("Last result", style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            val outcome = lastResult?.result.toOutcome()
            Text(text = outcome.label, color = outcome.color, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            DebugPayloadView(title = "Raw payload", payload = lastResult ?: lastError, maxHeight = 140.dp)
        }
    }
}

@Composable
private fun AdvancedOrderFields(
    payment: TabbyPayment,
    onPaymentChange: (TabbyPayment) -> Unit,
) {
    val shipping = payment.shippingAddress ?: ShippingAddress(address = "", city = "", zip = "")
    val order = payment.order ?: Order(refId = "")
    val item = order.items?.firstOrNull() ?: OrderItem(refId = "SKU-1", title = "")

    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text("Shipping address", style = MaterialTheme.typography.caption)
        OutlinedTextField(
            value = shipping.address,
            onValueChange = { onPaymentChange(payment.copy(shippingAddress = shipping.copy(address = it))) },
            label = { Text("Address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = shipping.city,
                onValueChange = { onPaymentChange(payment.copy(shippingAddress = shipping.copy(city = it))) },
                label = { Text("City") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = shipping.zip,
                onValueChange = { onPaymentChange(payment.copy(shippingAddress = shipping.copy(zip = it))) },
                label = { Text("ZIP") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(12.dp))
        Text("Order", style = MaterialTheme.typography.caption)
        OutlinedTextField(
            value = order.refId,
            onValueChange = {
                onPaymentChange(payment.copy(order = order.copy(refId = it)))
            },
            label = { Text("Order ref ID") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = item.title.orEmpty(),
            onValueChange = { text ->
                val newItem = item.copy(title = text)
                onPaymentChange(payment.copy(order = order.copy(items = listOf(newItem))))
            },
            label = { Text("Item title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = item.unitPrice.toPlainString(),
                onValueChange = { text ->
                    text.toBigDecimalOrNull()?.let {
                        val newItem = item.copy(unitPrice = it)
                        onPaymentChange(payment.copy(order = order.copy(items = listOf(newItem))))
                    }
                },
                label = { Text("Unit price") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = item.quantity.toString(),
                onValueChange = { text ->
                    text.toIntOrNull()?.let {
                        val newItem = item.copy(quantity = it)
                        onPaymentChange(payment.copy(order = order.copy(items = listOf(newItem))))
                    }
                },
                label = { Text("Quantity") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/**
 * Popup-free selectable chip. [DropdownMenu]/[androidx.compose.ui.window.Popup] is deliberately
 * avoided here — it can crash with "The ACTION_HOVER_EXIT event was not cleared" on devices that
 * synthesize hover events for touch (e.g. some Samsung/S-Pen hardware) on this Compose UI version.
 */
@Composable
private fun SelectableChip(label: String, selected: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = if (selected) MaterialTheme.colors.primary.copy(alpha = 0.15f) else MaterialTheme.colors.surface,
            contentColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
        ),
    ) {
        Text(label)
    }
}
