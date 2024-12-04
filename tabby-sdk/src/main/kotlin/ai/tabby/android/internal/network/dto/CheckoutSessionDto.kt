package ai.tabby.android.internal.network.dto

import ai.tabby.android.data.Product
import ai.tabby.android.data.ProductType
import ai.tabby.android.data.TabbySession
import ai.tabby.android.internal.network.CurrencyRest
import ai.tabby.android.internal.network.LangRest
import com.google.gson.annotations.SerializedName

internal data class CheckoutSessionDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("configuration")
    val config: ConfigurationDto,
    @SerializedName("payment")
    val payment: SessionPaymentDto,
    @SerializedName("status")
    val statusDto: String,
    @SerializedName("product_type")
    val productType: String,
    @SerializedName("lang")
    val language: LangRest,
    @SerializedName("merchant")
    val merchant: MerchantDto,
    @SerializedName("merchant_code")
    val merchantCode: String,
) {
    fun toSession(): TabbySession =
        TabbySession(
            id = id,
            paymentId = payment.id,
            availableProducts = config.availableProducts.toProductList()
        )
}

internal data class MerchantDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("logo")
    val logo: String? = null
)

internal data class ConfigurationDto(
    @SerializedName("currency")
    val currencyRest: CurrencyRest,
    @SerializedName("app_type")
    val appType: String,
    @SerializedName("new_customer")
    val newCustomer: String? = null,
    @SerializedName("available_limit")
    val availableLimit: String,
    @SerializedName("min_limit")
    val minLimit: String,
    @SerializedName("available_products")
    val availableProducts: ProductContainerDto
)

internal data class ProductContainerDto(
    @SerializedName("installments")
    val installmentsList: List<ProductDto>? = null,
    @SerializedName("credit_card_installments")
    val creditCardInstallments: List<ProductDto>? = null,
    @SerializedName("monthly_billing")
    val monthlyBilling: List<ProductDto>? = null
) {
    fun toProductList(): List<Product> =
        listOfNotNull(
            creditCardInstallments?.map { it.toProduct(ProductType.CREDIT_CARD_INSTALLMENTS) },
            installmentsList?.map { it.toProduct(ProductType.INSTALLMENTS) }
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