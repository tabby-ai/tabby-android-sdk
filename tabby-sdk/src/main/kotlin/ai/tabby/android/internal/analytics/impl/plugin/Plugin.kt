package ai.tabby.android.internal.analytics.impl.plugin

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent

internal interface Plugin {
    fun apply(event: AnalyticsEvent, params: Map<String, Any> = emptyMap())
}