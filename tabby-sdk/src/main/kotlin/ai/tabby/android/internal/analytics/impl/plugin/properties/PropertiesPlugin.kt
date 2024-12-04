package ai.tabby.android.internal.analytics.impl.plugin.properties

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent
import ai.tabby.android.internal.analytics.impl.Parameters
import ai.tabby.android.internal.analytics.impl.EventProperties
import ai.tabby.android.internal.analytics.impl.apply
import ai.tabby.android.internal.analytics.impl.plugin.Plugin

private object PropertiesPlugin : Plugin {
    private const val PROPERTIES_KEY: String = "properties"

    @Suppress("UNCHECKED_CAST")
    override fun apply(event: AnalyticsEvent, params: EventProperties) {
        val eventProperties: Parameters = event.getOrElse(PROPERTIES_KEY) {
            emptyMap<String, Any>()
        } as? Parameters ?: emptyMap()

        val parameters = mapOf(
            PROPERTIES_KEY to eventProperties.applyProperties(params)
        )

        event.apply(parameters)
    }
}

internal fun EventProperties.applyProperties(properties: EventProperties): EventProperties {
    return toMutableMap().apply {
        putAll(properties)
    }
}

internal fun AnalyticsEvent.applyProperties(properties: EventProperties) {
    PropertiesPlugin.apply(this, properties)
}