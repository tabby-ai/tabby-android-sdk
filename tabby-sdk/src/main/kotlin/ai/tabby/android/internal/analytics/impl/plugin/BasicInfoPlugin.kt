package ai.tabby.android.internal.analytics.impl.plugin

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent
import ai.tabby.android.internal.analytics.impl.Parameters
import ai.tabby.android.internal.analytics.impl.apply
import java.util.UUID

internal object BasicInfoPlugin : Plugin {
    private val sessionId: String = UUID.randomUUID().toString()

    override fun apply(event: AnalyticsEvent, params: Parameters) {
        event.apply(
            mapOf(
                "anonymousId" to sessionId,
                "mobileSDK" to true,
            )
        )
    }
}