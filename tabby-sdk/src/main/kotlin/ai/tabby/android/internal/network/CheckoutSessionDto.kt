package ai.tabby.android.internal.network

import ai.tabby.android.data.*
import com.google.gson.annotations.SerializedName

internal data class CheckoutSessionDto(

    @SerializedName("id")
    val id: String,

    @SerializedName("configuration")
    val config: ConfigurationDto,

    @SerializedName("payment")
    val payment: SessionPaymentDto,
) {
    fun toSession(): TabbySession =
        TabbySession(
            id = id,
            paymentId = payment.id,
            availableProducts = config.availableProducts.toProductList()
        )
}

internal data class ConfigurationDto(

    @SerializedName("available_products")
    val availableProducts: ProductContainerDto
)

internal data class ProductContainerDto(

    @SerializedName("installments")
    val installmentsList: List<ProductDto>? = null,

    @SerializedName("pay_later")
    val payLaterList: List<ProductDto>? = null,
) {
    fun toProductList(): List<Product> =
        listOfNotNull(
            installmentsList?.map { it.toProduct(ProductType.INSTALLMENTS) },
            payLaterList?.map { it.toProduct(ProductType.PAY_LATER) }
        ).flatten()
}

internal data class ProductDto(

    @SerializedName("web_url")
    val webUrl: String
) {
    fun toProduct(type: ProductType) =
        Product(
            type = type,
            webUrl = webUrl
        )
}

internal data class SessionPaymentDto(

    @SerializedName("id")
    val id: String
)