package ai.tabby.android.internal.network

import ai.tabby.android.data.*
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

internal data class CheckoutPayloadDto(

    @SerializedName("merchant_code")
    val merchantCode: String,

    @SerializedName("lang")
    val lang: LangRest,

    @SerializedName("payment")
    val payment: PaymentDto

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

internal data class PaymentDto(

    @SerializedName("amount")
    val amount: BigDecimal,

    @SerializedName("currency")
    val currency: CurrencyRest,

    @SerializedName("description")
    val description: String,

    @SerializedName("buyer")
    val buyer: BuyerDto,

    @SerializedName("order")
    val order: OrderDto? = null,

    @SerializedName("shipping_address")
    val shippingAddress: ShippingAddressDto? = null,

    ) {
    companion object {
        fun fromPayment(p: TabbyPayment): PaymentDto =
            PaymentDto(
                amount = p.amount,
                currency = CurrencyRest.fromCurrency(p.currency),
                description = p.description,
                buyer = BuyerDto.fromBuyer(p.buyer),
                order = p.order?.let { OrderDto.fromOrder(it) },
                shippingAddress = p.shippingAddress?.let { ShippingAddressDto.fromShippingAddress(it) }
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

    @SerializedName("reference_id")
    val refId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("product_url")
    val productUrl: String,

    @SerializedName("unit_price")
    val unitPrice: BigDecimal,

    @SerializedName("quantity")
    val quantity: Int,

    ) {
    companion object {
        fun fromOrderItem(i: OrderItem): OrderItemDto =
            OrderItemDto(
                refId = i.refId,
                title = i.title,
                description = i.description,
                productUrl = i.productUrl,
                unitPrice = i.unitPrice,
                quantity = i.quantity
            )
    }
}

internal data class ShippingAddressDto(

    @SerializedName("address")
    val address: String,

    @SerializedName("city")
    val city: String,

    ) {
    companion object {
        fun fromShippingAddress(a: ShippingAddress): ShippingAddressDto =
            ShippingAddressDto(
                address = a.address,
                city = a.city
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
