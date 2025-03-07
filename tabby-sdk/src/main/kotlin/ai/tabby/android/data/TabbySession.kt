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
    val status: Status,
    val paymentId: String,
    val availableProducts: List<Product>
) : Parcelable {

    enum class Status {
        Created,
        NotAvailable,
        OrderAmountTooHigh,
        OrderAmountTooLow,
        Rejected,
    }
}

@Parcelize
data class Product(
    val type: ProductType,
    val webUrl: String
) : Parcelable

enum class ProductType {
    INSTALLMENTS,
    CREDIT_CARD_INSTALLMENTS
}

