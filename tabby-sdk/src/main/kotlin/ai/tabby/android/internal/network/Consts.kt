package ai.tabby.android.internal.network

import ai.tabby.android.data.Currency
import ai.tabby.android.data.Lang
import ai.tabby.android.data.ProductType
import com.google.gson.annotations.SerializedName

internal enum class LangRest(
    val publicLang: Lang
) {
    @SerializedName("en") EN(Lang.EN),
    @SerializedName("ar") AR(Lang.AR), ;

    companion object {
        fun fromLang(l: Lang): LangRest =
            values().find { it.publicLang == l }
                ?: throw IllegalArgumentException("Language $l not found")
    }
}

internal enum class CurrencyRest(
    val publicCurrency: Currency
) {
    @SerializedName("AED") AED(Currency.AED),
    @SerializedName("SAR") SAR(Currency.SAR),
    @SerializedName("BHD") BHD(Currency.BHD),
    @SerializedName("KWD") KWD(Currency.KWD),
    @SerializedName("EGP") EGP(Currency.EGP);

    companion object {
        fun fromCurrency(c: Currency): CurrencyRest =
            values().find { it.publicCurrency == c }
                ?: throw IllegalArgumentException("Currency $c not found")
    }
}

internal enum class ProductTypeRest(
    val publicProductType: ProductType
) {
    @SerializedName("installments") INSTALLMENTS(ProductType.INSTALLMENTS),
    @SerializedName("credit_card_installments") CREDIT_CART_INSTALLMENTS(ProductType.CREDIT_CARD_INSTALLMENTS);

    companion object {
        fun fromProductType(p: ProductType): ProductTypeRest =
            values().find { it.publicProductType == p }
                ?: throw IllegalArgumentException("Product type $p not found")
    }
}
