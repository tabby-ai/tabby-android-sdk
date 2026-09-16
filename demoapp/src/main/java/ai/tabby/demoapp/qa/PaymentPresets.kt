package ai.tabby.demoapp.qa

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
import ai.tabby.android.internal.utils.TabbyLanguageResolver
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

/**
 * One-tap test-data presets for the QA payment builder. The stage/test backend keys the outcome
 * of a session off [Buyer.email] ("successful.payment@tabby.ai" always authorizes, "rejected@..."
 * is always rejected) — pick a preset instead of hand-typing a magic email.
 */
object PaymentPresets {

    enum class Preset(val label: String) {
        SUCCESSFUL("Successful"),
        REJECTED("Rejected"),
        BLANK("Blank"),
    }

    fun payment(preset: Preset): TabbyPayment = when (preset) {
        Preset.SUCCESSFUL -> successful()
        Preset.REJECTED -> rejected()
        Preset.BLANK -> blank()
    }

    fun successful() = TabbyPayment(
        amount = BigDecimal(340),
        currency = Currency.AED,
        description = "tabby Store Order #33",
        buyer = Buyer(
            email = "successful.payment@tabby.ai", // Always authorized on stage
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
                TabbyLanguageResolver.getNumberLocale()
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

    fun rejected() = TabbyPayment(
        amount = BigDecimal(340),
        currency = Currency.AED,
        description = "tabby Store Order #33",
        buyer = Buyer(
            email = "rejected@tabby.ai", // Always rejected on stage
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
            registeredSince = GregorianCalendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -7)
            }.time,
            loyaltyLevel = 0,
        ),
        orderHistory = emptyList(),
        meta = emptyMap(),
    )

    fun blank() = TabbyPayment(
        amount = BigDecimal.ZERO,
        currency = Currency.AED,
        description = null,
        buyer = Buyer(
            email = "",
            phone = "",
            name = "",
        ),
        order = null,
        shippingAddress = null,
        buyerHistory = BuyerHistory(
            registeredSince = Date(),
            loyaltyLevel = 0,
        ),
        orderHistory = emptyList(),
        meta = emptyMap(),
    )
}
