package ai.tabby.android.internal.analytics.impl.plugin

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent
import ai.tabby.android.internal.analytics.impl.Parameters
import ai.tabby.android.internal.analytics.impl.plugin.properties.applyProperties

internal class InternalPropertiesPlugin(
    private val apiKey: String
) : Plugin {
    override fun apply(event: AnalyticsEvent, params: Parameters) {
        event.applyProperties(
            mapOf(
                "publicKey" to apiKey
            )
        )
    }
}