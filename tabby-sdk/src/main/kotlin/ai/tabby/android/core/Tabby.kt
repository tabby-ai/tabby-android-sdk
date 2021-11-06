package ai.tabby.android.core

import ai.tabby.android.data.Lang
import ai.tabby.android.data.Payment
import ai.tabby.android.data.Product
import ai.tabby.android.data.Session
import android.content.Intent

interface Tabby {

    suspend fun createSession(
        merchantCode: String,
        lang: Lang,
        payment: Payment
    ): Session

    fun createCheckoutIntent(
        product: Product
    ): Intent
}
