package ai.tabby.android.internal.analytics.impl.plugin

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent
import ai.tabby.android.internal.analytics.impl.Parameters
import ai.tabby.android.internal.analytics.impl.plugin.properties.applyIntegrationsProperties

internal object IntegrationsPlugin : Plugin {
    override fun apply(event: AnalyticsEvent, params: Parameters) {
        event.applyIntegrationsProperties(
            mapOf(
                "Segment.io" to true
            )
        )
    }
}