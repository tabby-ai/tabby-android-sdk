package ai.tabby.android.core

import ai.tabby.android.data.Lang
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.data.Product
import ai.tabby.android.data.TabbySession
import android.content.Intent

interface Tabby {

    suspend fun createSession(
        merchantCode: String,
        lang: Lang,
        payment: TabbyPayment
    ): TabbySession

    fun createCheckoutIntent(
        product: Product
    ): Intent
}
