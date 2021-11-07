package ai.tabby.android.data

import java.math.BigDecimal

data class TabbyPayment(

    val amount: BigDecimal,

    val currency: Currency,

    val description: String,

    val buyer: Buyer,

    val order: Order? = null,

    val shippingAddress: ShippingAddress? = null,
)

data class Order(

    val refId: String,

    val items: List<OrderItem>? = null,

    val shippingAmount: BigDecimal? = null,

    val taxAmount: BigDecimal? = null,
)

data class OrderItem(

    val refId: String,

    val title: String,

    val description: String,

    val productUrl: String,

    val unitPrice: BigDecimal,

    val quantity: Int,
)

data class ShippingAddress(

    val address: String,

    val city: String,
)

data class Buyer(

    val email: String,

    val phone: String,

    val name: String,

    val dob: String? = null,
)