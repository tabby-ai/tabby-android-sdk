package ai.tabby.android.internal.core

import ai.tabby.android.core.Tabby
import ai.tabby.android.data.Lang
import ai.tabby.android.data.Payment
import ai.tabby.android.data.Product
import ai.tabby.android.data.Session
import ai.tabby.android.internal.logger.TabbyLogger
import ai.tabby.android.internal.network.CheckoutPayloadDto
import ai.tabby.android.internal.network.TabbyService
import ai.tabby.android.internal.ui.CheckoutActivity
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TabbyImpl @Inject constructor(
    private val context: Context,
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
        logger.v(TAG) { "Creating session from mc: '$merchantCode', lang: '$lang', payment: $payment"}
        val payload = CheckoutPayloadDto.fromPaymentAndParams(
            merchantCode = merchantCode,
            lang = lang,
            payment = payment
        )
        logger.v(TAG) { "Session payload $payload" }
        val bearerApiKey = "Bearer $apiKey"
        val sessionDto = withContext(ioDispatcher) {
            tabbyService.createSession(
                apiKey = bearerApiKey,
                request = payload
            )
        }
        val session = sessionDto.toSession()
        logger.v(TAG) { "Session is created: $session" }
        return session
    }

    override fun createCheckoutIntent(
        product: Product
    ): Intent =
        Intent(context, CheckoutActivity::class.java)

}