package ai.tabby.android.internal.core

import ai.tabby.android.Tabby
import ai.tabby.android.data.Lang
import ai.tabby.android.data.Payment
import ai.tabby.android.data.Session
import ai.tabby.android.internal.network.CheckoutPayloadDto
import ai.tabby.android.internal.network.TabbyService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TabbyImpl @Inject constructor(
    private val apiKey: String,
    private val tabbyService: TabbyService,
    private val ioDispatcher: CoroutineDispatcher
): Tabby {

    override suspend fun createSession(
        merchantCode: String,
        lang: Lang,
        payment: Payment
    ): Session {
        val payload = CheckoutPayloadDto.fromPaymentAndParams(
            merchantCode = merchantCode,
            lang = lang,
            payment = payment
        )
        val sessionDto = withContext(ioDispatcher) {
            tabbyService.createSession(
                apiKey = apiKey,
                request = payload
            )
        }
        return sessionDto.toSession()
    }

}