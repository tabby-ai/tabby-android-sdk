package ai.tabby.android

import ai.tabby.android.data.Lang
import ai.tabby.android.data.Payment
import ai.tabby.android.data.Session

interface Tabby {

    suspend fun createSession(
        merchantCode: String,
        lang: Lang,
        payment: Payment
    ): Session

}
