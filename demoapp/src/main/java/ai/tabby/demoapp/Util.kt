package ai.tabby.demoapp

import ai.tabby.android.data.Attachment
import ai.tabby.android.data.Buyer
import ai.tabby.android.data.BuyerHistory
import ai.tabby.android.data.Currency
import ai.tabby.android.data.Order
import ai.tabby.android.data.OrderHistory
import ai.tabby.android.data.OrderItem
import ai.tabby.android.data.PaymentMethod
import ai.tabby.android.data.ShippingAddress
import ai.tabby.android.data.Status
import ai.tabby.android.data.TabbyPayment
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

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
        registeredSince = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.getDefault()
        ).parse("2019-08-24T14:15:22Z")!!,
        loyaltyLevel = 0,
        wishlistCount = 1,
        isSocialNetworksConnected = false,
        isPhoneNumberVerified = false,
        isEmailVerified = false,
    ),
    orderHistory = listOf(
        OrderHistory(
            purchasedAt = GregorianCalendar.getInstance().apply {
                set(2019, 8, 24)
            }.time,
            amount = BigDecimal.ONE,
            paymentMethod = PaymentMethod.CARD,
            status = Status.NEW,
            buyer = Buyer(
                email = "test@gmail.com",
                phone = "+995555466567",
                name = "Denis",
                dob = "2019-08-24",
            ),
            shippingAddress = ShippingAddress(
                address = "Tbel-Abuseridze",
                city = "Batumi",
                zip = "6010",
            ),
            items = listOf(
                OrderItem(
                    refId = "1242532",
                    title = "Test item"
                )
            ),
        )
    ),
    meta = emptyMap(),
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
        registeredSince = Date(LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC)),
        loyaltyLevel = 0,
    ),
    orderHistory = emptyList(),
    meta = emptyMap(),
)
