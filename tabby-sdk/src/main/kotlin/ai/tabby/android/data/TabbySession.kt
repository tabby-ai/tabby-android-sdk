package ai.tabby.android.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Tabby session returned by the [createSession][ai.tabby.android.core.Tabby.createSession]
 *
 * @see ai.tabby.android.core.Tabby.createSession
 */
@Parcelize
data class TabbySession(

    val id: String,

    val paymentId: String,

    val availableProducts: List<Product>

) : Parcelable

@Parcelize
data class Product(

    val type: ProductType,

    val webUrl: String

) : Parcelable

enum class ProductType {
    PAY_LATER,
    INSTALLMENTS,
}

