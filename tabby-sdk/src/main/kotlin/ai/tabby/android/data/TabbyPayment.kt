package ai.tabby.android.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class TabbyPayment(

    val amount: BigDecimal,

    val currency: Currency,

    val description: String,

    val buyer: Buyer,

    val order: Order? = null,

    val shippingAddress: ShippingAddress? = null

) : Parcelable

@Parcelize
data class Order(

    val refId: String,

    val items: List<OrderItem>? = null,

    val shippingAmount: BigDecimal? = null,

    val taxAmount: BigDecimal? = null

) : Parcelable

@Parcelize
data class OrderItem(

    val refId: String,

    val title: String,

    val description: String,

    val productUrl: String,

    val unitPrice: BigDecimal,

    val quantity: Int

) : Parcelable

@Parcelize
data class ShippingAddress(

    val address: String,

    val city: String

) : Parcelable

@Parcelize
data class Buyer(

    val email: String,

    val phone: String,

    val name: String,

    val dob: String? = null

) : Parcelable