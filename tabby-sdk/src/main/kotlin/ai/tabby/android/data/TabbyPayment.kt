package ai.tabby.android.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.Date

/**
 * Root class of Tabby Payment Configuration.
 *
 * @see ai.tabby.android.core.Tabby.createSession
 */
@Parcelize
data class TabbyPayment(
    val amount: BigDecimal,
    val currency: Currency,
    val description: String? = null,
    val buyer: Buyer,
    val shippingAddress: ShippingAddress? = null,
    val order: Order? = null,
    val buyerHistory: BuyerHistory,
    val orderHistory: List<OrderHistory>,
    val meta: Map<String, String>,
    val attachment: Attachment? = null
) : Parcelable

@Parcelize
data class Attachment(
    val body: String,
    val contentType: String
) : Parcelable

@Parcelize
data class OrderHistory(
    val purchasedAt: Date,
    val amount: BigDecimal,
    val paymentMethod: PaymentMethod? = null,
    val status: Status,
    val buyer: Buyer,
    val shippingAddress: ShippingAddress,
    val items: List<OrderItem>
) : Parcelable

enum class PaymentMethod(val method: String) {
    CARD("card"),
    COD("cod")
}

enum class Status(val status: String) {
    NEW("new"),
    PROCESSING("processing"),
    COMPLETE("complete"),
    REFUNDED("refunded"),
    CANCELLED("canceled"),
    UNKNOWN("unknown"),
}

@Parcelize
data class BuyerHistory(
    val registeredSince: Date,
    val loyaltyLevel: Int,
    val wishlistCount: Int? = null,
    val isSocialNetworksConnected: Boolean? = null,
    val isPhoneNumberVerified: Boolean? = null,
    val isEmailVerified: Boolean? = null,
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
    val title: String? = null,
    val description: String? = null,
    val quantity: Int = 1,
    val unitPrice: BigDecimal = BigDecimal(0.00),
    val discount: BigDecimal = BigDecimal(0.00),
    val refId: String,
    val imageUrl: String? = null,
    val productUrl: String? = null,
    val ordered: Int = 0,
    val captured: Int = 0,
    val shipped: Int = 0,
    val refunded: Int = 0,
    val gender: String? = null,
    val category: String? = null,
    val color: String? = null,
    val productMaterial: String? = null,
    val sizeType: String? = null,
    val size: String? = null,
    val brand: String? = null
) : Parcelable

@Parcelize
data class ShippingAddress(
    val address: String,
    val city: String,
    val zip: String
) : Parcelable

@Parcelize
data class Buyer(
    val email: String,
    val phone: String,
    val name: String,
    val dob: String? = null
) : Parcelable