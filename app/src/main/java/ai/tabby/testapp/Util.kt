package ai.tabby.testapp

import ai.tabby.android.data.*
import java.math.BigDecimal

fun createDefaultPayment() = TabbyPayment(
    amount = BigDecimal(340),
    currency = Currency.AED,
    description = "tabby Store Order #33",
    buyer = Buyer(
        email = "daniil.barkalov@tabby.ai",
        phone = "+971585829064",
        name = "Daniil Barkalov"
    ),
    order = Order(
        refId = "#xxxx-xxxxxx-xxxx",
        items = listOf(
            OrderItem(
                refId = "SKU123",
                title = "Pink jersey",
                description = "Jersey",
                productUrl = "https://tabby.store/p/SKU123",
                unitPrice = BigDecimal(300),
                quantity = 1
            )
        ),
        shippingAmount = BigDecimal(50),
        taxAmount = BigDecimal(100)
    ),
    shippingAddress = ShippingAddress(
        address = "Sample Address #2",
        city = "Dubai"
    )
)