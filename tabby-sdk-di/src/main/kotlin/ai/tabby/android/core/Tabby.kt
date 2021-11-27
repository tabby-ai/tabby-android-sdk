package ai.tabby.android.core

import ai.tabby.android.data.Lang
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.data.Product
import ai.tabby.android.data.TabbySession
import android.content.Intent

/**
 * Provides various methods for operations with Tabby.
 */
interface Tabby {

    /**
     * Creates [TabbySession] session. Usually merchant's app calls this method from a checkout screen.
     * Merchant should provide all necessary information about the purchase. Method returns
     * [TabbySession] that contains available Tabby products, which can be displayed
     * on a checkout screen where customer can choose one of them.
     *
     * @param merchantCode Merchant code, usually `"ae"` or `"en"`
     * @param lang Language, see [Lang]
     * @param payment Payment configuration
     *
     * @see TabbySession
     * @see TabbyPayment
     * @see Lang
     */
    suspend fun createSession(
        merchantCode: String,
        lang: Lang,
        payment: TabbyPayment
    ): TabbySession

    /**
     * Creates intent for Tabby Checkout. Merchant's app usually starts activity for result using
     * returned intent.
     *
     * @param product Product, selected by a customer on a checkout screen
     *
     * @see Product
     */
    fun createCheckoutIntent(
        product: Product
    ): Intent
}
