package ai.tabby.android.internal.core

import ai.tabby.android.Tabby
import ai.tabby.android.data.Lang
import ai.tabby.android.data.Payment
import ai.tabby.android.data.Session
import ai.tabby.android.internal.logger.TabbyLogger
import ai.tabby.android.internal.network.CheckoutPayloadDto
import ai.tabby.android.internal.network.TabbyService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TabbyImpl @Inject constructor(
    private val apiKey: String,
    private val tabbyService: TabbyService,
    private val ioDispatcher: CoroutineDispatcher,
    private val logger: TabbyLogger
): Tabby {

    companion object {
        private const val TAG = "Tabby"
    }

    override suspend fun createSession(
        merchantCode: String,
        lang: Lang,
        payment: Payment
    ): Session {
        logger.logV(TAG) { "Creating session from mc: '$merchantCode', lang: '$lang', payment: $payment"}
        val payload = CheckoutPayloadDto.fromPaymentAndParams(
            merchantCode = merchantCode,
            lang = lang,
            payment = payment
        )
        logger.logV(TAG) { "Session payload $payload" }
        val sessionDto = withContext(ioDispatcher) {
            tabbyService.createSession(
                apiKey = apiKey,
                request = payload
            )
        }
        val session = sessionDto.toSession()
        logger.logV(TAG) { "Session is created: $session" }
        return session
    }

}