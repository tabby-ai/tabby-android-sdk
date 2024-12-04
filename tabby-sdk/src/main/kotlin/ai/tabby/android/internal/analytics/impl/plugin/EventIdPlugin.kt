package ai.tabby.android.internal.analytics.impl.plugin

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent
import ai.tabby.android.internal.analytics.impl.Parameters
import ai.tabby.android.internal.analytics.impl.apply
import ai.tabby.android.internal.utils.DateHelper
import java.util.TimeZone
import java.util.UUID

internal object EventIdPlugin : Plugin {
    override fun apply(event: AnalyticsEvent, params: Parameters) {
        event.apply(
            mapOf(
                "messageId" to UUID.randomUUID().toString(),
                "timestamp" to DateHelper.now(TimeZone.getTimeZone("UTC"))
            )
        )
    }
}