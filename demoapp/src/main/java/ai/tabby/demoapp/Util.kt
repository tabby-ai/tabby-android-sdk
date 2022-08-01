package ai.tabby.demoapp

import ai.tabby.android.data.*
import java.math.BigDecimal

fun createSuccessfulPayment() = TabbyPayment(
    amount = BigDecimal(340),
    currency = Currency.AED,
    description = "tabby Store Order #33",
    buyer = Buyer(
        email = "successful.payment@tabby.ai",  // SUCCESSFUL
        phone = "500000001",
        name = "Yazan Khalid"
    ),
    order = Order(
        refId = "#xxxx-xxxxxx-xxxx",
        items = listOf(
            OrderItem(
                refId = "SKU123",
                title = "Pink jersey",
                description = "Jersey",
                productUrl = "https://tabby.store/p/SKU123",
                unitPrice = BigDecimal(300.00),
                quantity = 1
            )
        ),
        shippingAmount = BigDecimal(50),
        taxAmount = BigDecimal(100)
    ),
    shippingAddress = ShippingAddress(
        address = "Sample Address #2",
        city = "Dubai",
        zip = "11111"
    ),
    buyerHistory = BuyerHistory(
        registeredSince = "2019-08-24T14:15:22Z",
        loyaltyLevel = 0,
    ),
    meta = mapOf(),
    orderHistory = listOf()
)

fun createRejectedPayment() = TabbyPayment(
    amount = BigDecimal(340),
    currency = Currency.AED,
    description = "tabby Store Order #33",
    buyer = Buyer(
        email = "rejected@tabby.ai ",   // REJECTED
        phone = "500000001",
        name = "Yazan Khalid"
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
        city = "Dubai",
        zip = "11111"
    ),
    attachment = Attachment(
        body = "",
        contentType = ""
    ),
    buyerHistory = BuyerHistory(
        registeredSince = "2019-08-24T14:15:22Z",
        loyaltyLevel = 0,
    ),
    meta = mapOf(),
    orderHistory = listOf()
)
