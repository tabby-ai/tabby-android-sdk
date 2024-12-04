package ai.tabby.android.internal.analytics.impl

import ai.tabby.android.internal.analytics.api.Event

internal typealias AnalyticsEvent = HashMap<String, Any>
internal typealias Parameters = Map<String, Any>
internal typealias EventProperties = Map<String, Any>
internal typealias ContextProperties = Map<String, Any>
internal typealias IntegrationsProperties = Map<String, Any>

internal fun AnalyticsEvent.apply(params: Parameters): Unit = putAll(params)
internal fun AnalyticsEvent.apply(event: Event) {
    put("event", event.name)
    put("type", event.type)
}


