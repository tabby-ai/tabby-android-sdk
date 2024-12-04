package ai.tabby.android.internal.analytics.impl.plugin

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent
import ai.tabby.android.internal.analytics.impl.Parameters
import ai.tabby.android.internal.analytics.impl.plugin.properties.applyContextProperties

internal object EventContextPlugin : Plugin {
    override fun apply(event: AnalyticsEvent, params: Parameters) {
        event.applyContextProperties(
            mapOf(
                "source" to "android-sdk",
                "direct" to true,
            )
        )
    }
}