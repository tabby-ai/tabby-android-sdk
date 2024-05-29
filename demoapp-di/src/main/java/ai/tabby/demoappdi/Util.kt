package ai.tabby.demoappdi

import ai.tabby.android.data.Buyer
import ai.tabby.android.data.BuyerHistory
import ai.tabby.android.data.Currency
import ai.tabby.android.data.Order
import ai.tabby.android.data.OrderItem
import ai.tabby.android.data.ShippingAddress
import ai.tabby.android.data.TabbyPayment
import java.math.BigDecimal
import java.util.GregorianCalendar

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
    buyerHistory = BuyerHistory(
        registeredSince = GregorianCalendar.getInstance().apply {
            set(2019, 8, 24)
        }.time,
        loyaltyLevel = 0,
    ),
    meta = emptyMap(),
    orderHistory = emptyList(),
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
    buyerHistory = BuyerHistory(
        registeredSince = GregorianCalendar.getInstance().apply {
            set(2019, 8, 24)
        }.time,
        loyaltyLevel = 0,
    ),
    meta = emptyMap(),
    orderHistory = emptyList(),
)
