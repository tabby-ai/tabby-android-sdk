package ai.tabby.android.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class ProductType {
    PAY_LATER,
    INSTALLMENTS,
}

@Parcelize
data class TabbySession(

    val id: String,

    val availableProducts: List<Product>

) : Parcelable

@Parcelize
data class Product(

    val type: ProductType,

    val webUrl: String

) : Parcelable
