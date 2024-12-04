package ai.tabby.android.internal.network.dto

import ai.tabby.android.data.Attachment
import ai.tabby.android.data.Buyer
import ai.tabby.android.data.BuyerHistory
import ai.tabby.android.data.Lang
import ai.tabby.android.data.Order
import ai.tabby.android.data.OrderHistory
import ai.tabby.android.data.OrderItem
import ai.tabby.android.data.ShippingAddress
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.internal.network.CurrencyRest
import ai.tabby.android.internal.network.LangRest
import ai.tabby.android.internal.utils.DateHelper.toIsoStringUTC
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

internal data class CheckoutPayloadDto(
    @SerializedName("payment")
    val payment: PaymentDto,
    @SerializedName("lang")
    val lang: LangRest,
    @SerializedName("merchant_code")
    val merchantCode: String,
    @SerializedName("merchant_urls")
    val merchantUrls: MerchantUrlsDto? = null
) {
    companion object {
        fun fromPaymentAndParams(
            merchantCode: String,
            lang: Lang,
            payment: TabbyPayment
        ): CheckoutPayloadDto =
            CheckoutPayloadDto(
                merchantCode = merchantCode,
                lang = LangRest.fromLang(lang),
                payment = PaymentDto.fromPayment(payment)
            )
    }
}

internal data class MerchantUrlsDto(
    @SerializedName("success")
    val success: String,
    @SerializedName("cancel")
    val cancel: String,
    @SerializedName("failure")
    val failure: String
)

internal data class PaymentDto(
    @SerializedName("amount")
    val amount: BigDecimal,
    @SerializedName("currency")
    val currency: CurrencyRest,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("buyer")
    val buyer: BuyerDto,
    @SerializedName("shipping_address")
    val shippingAddress: ShippingAddressDto? = null,
    @SerializedName("order")
    val order: OrderDto? = null,
    @SerializedName("buyer_history")
    val buyerHistory: BuyerHistoryDto,
    @SerializedName("order_history")
    val orderHistory: List<OrderHistoryDto>,
    @SerializedName("meta")
    val meta: Map<String, String>,
    @SerializedName("attachment")
    val attachment: AttachmentDto
) {
    companion object {
        fun fromPayment(p: TabbyPayment): PaymentDto =
            PaymentDto(
                amount = p.amount,
                currency = CurrencyRest.fromCurrency(p.currency),
                description = p.description,
                buyer = BuyerDto.fromBuyer(p.buyer),
                order = p.order?.let { OrderDto.fromOrder(it) },
                shippingAddress = p.shippingAddress?.let { ShippingAddressDto.fromShippingAddress(it) },
                buyerHistory = BuyerHistoryDto.fromBuyerHistory(p.buyerHistory),
                orderHistory = p.orderHistory.map { OrderHistoryDto.fromOrderHistory(it) },
                meta = p.meta,
                attachment = p.attachment?.let { AttachmentDto.fromAttachment(it) }
                    ?: AttachmentDto()
            )
    }
}

internal data class AttachmentDto(
    @SerializedName("body")
    val body: String = "{\"flight_reservation_details\": {\"pnr\": \"TR9088999\",\"itinerary\": [...],\"insurance\": [...],\"passengers\": [...],\"affiliate_name\": \"some affiliate\"}}",
    @SerializedName("content_type")
    val contentType: String = "application/vnd.tabby.v1+json"
) {
    companion object {
        fun fromAttachment(attachment: Attachment): AttachmentDto = AttachmentDto(
            body = attachment.body,
            contentType = attachment.contentType
        )
    }
}

internal data class BuyerHistoryDto(
    @SerializedName("registered_since")
    val registeredSince: String,
    @SerializedName("loyalty_level")
    val loyaltyLevel: Int,
    @SerializedName("wishlist_count")
    val wishlistCount: Int? = null,
    @SerializedName("is_social_networks_connected")
    val isSocialNetworksConnected: Boolean? = null,
    @SerializedName("is_phone_number_verified")
    val isPhoneNumberVerified: Boolean? = null,
    @SerializedName("is_email_verified")
    val isEmailVerified: Boolean? = null,
) {
    companion object {
        fun fromBuyerHistory(buyerHistory: BuyerHistory): BuyerHistoryDto = BuyerHistoryDto(
            registeredSince = buyerHistory.registeredSince.toIsoStringUTC(),
            loyaltyLevel = buyerHistory.loyaltyLevel,
            wishlistCount = buyerHistory.wishlistCount,
            isSocialNetworksConnected = buyerHistory.isSocialNetworksConnected,
            isPhoneNumberVerified = buyerHistory.isPhoneNumberVerified,
            isEmailVerified = buyerHistory.isEmailVerified,
        )
    }
}

internal data class OrderHistoryDto(
    @SerializedName("purchased_at")
    val purchasedAt: String,
    @SerializedName("amount")
    val amount: BigDecimal,
    @SerializedName("payment_method")
    val paymentMethod: String? = null,
    @SerializedName("status")
    val status: String,
    @SerializedName("buyer")
    val buyer: BuyerDto,
    @SerializedName("shipping_address")
    val shippingAddress: ShippingAddressDto,
    @SerializedName("items")
    val items: List<OrderItemDto>
) {
    companion object {
        fun fromOrderHistory(orderHistory: OrderHistory) =
            OrderHistoryDto(
                purchasedAt = orderHistory.purchasedAt.toIsoStringUTC(),
                amount = orderHistory.amount,
                paymentMethod = orderHistory.paymentMethod?.method,
                status = orderHistory.status.status,
                buyer = BuyerDto.fromBuyer(orderHistory.buyer),
                shippingAddress = ShippingAddressDto.fromShippingAddress(orderHistory.shippingAddress),
                items = orderHistory.items.map { OrderItemDto.fromOrderItem(it) }
            )
    }
}

internal data class OrderDto(
    @SerializedName("reference_id")
    val refId: String,
    @SerializedName("items")
    val items: List<OrderItemDto>? = null,
    @SerializedName("shipping_amount")
    val shippingAmount: BigDecimal? = null,
    @SerializedName("tax_amount")
    val taxAmount: BigDecimal? = null,
) {
    companion object {
        fun fromOrder(o: Order): OrderDto =
            OrderDto(
                refId = o.refId,
                items = o.items?.map { OrderItemDto.fromOrderItem(it) },
                shippingAmount = o.shippingAmount,
                taxAmount = o.taxAmount
            )
    }
}

internal data class OrderItemDto(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("quantity")
    val quantity: Int = 1,
    @SerializedName("unit_price")
    val unitPrice: BigDecimal = BigDecimal(0.00),
    @SerializedName("discount_amount")
    val discount: BigDecimal = BigDecimal(0.00),
    @SerializedName("reference_id")
    val refId: String,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("product_url")
    val productUrl: String? = null,
    @SerializedName("ordered")
    val ordered: Int = 0,
    @SerializedName("captured")
    val captured: Int = 0,
    @SerializedName("shipped")
    val shipped: Int = 0,
    @SerializedName("refunded")
    val refunded: Int = 0,
    @SerializedName("gender")
    val gender: GenderDto? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("product_material")
    val productMaterial: String? = null,
    @SerializedName("size_type")
    val sizeType: String? = null,
    @SerializedName("size")
    val size: String? = null,
    @SerializedName("brand")
    val brand: String? = null
) {
    companion object {
        fun fromOrderItem(i: OrderItem): OrderItemDto =
            OrderItemDto(
                refId = i.refId,
                title = i.title,
                description = i.description,
                productUrl = i.productUrl,
                unitPrice = i.unitPrice,
                quantity = i.quantity,
                discount = i.discount,
                size = i.size,
                sizeType = i.sizeType,
                brand = i.brand,
                productMaterial = i.productMaterial,
                color = i.color,
                gender = i.gender?.let { GenderDto.valueOf(it) },
                ordered = i.ordered,
                shipped = i.shipped,
                captured = i.captured,
                refunded = i.refunded,
                imageUrl = i.imageUrl,
                category = i.category
            )
    }
}

internal enum class GenderDto(val string: String) {
    MALE("Male"), FEMALE("Female"), KIDS("Kids"), OTHER("Other")
}

internal data class ShippingAddressDto(
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("zip")
    val zip: String
) {
    companion object {
        fun fromShippingAddress(a: ShippingAddress): ShippingAddressDto =
            ShippingAddressDto(
                address = a.address,
                city = a.city,
                zip = a.zip
            )
    }
}

internal data class BuyerDto(

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("dob")
    val dob: String? = null,

    ) {
    companion object {
        fun fromBuyer(b: Buyer): BuyerDto =
            BuyerDto(
                email = b.email,
                phone = b.phone,
                name = b.name,
                dob = b.dob
            )
    }
}
