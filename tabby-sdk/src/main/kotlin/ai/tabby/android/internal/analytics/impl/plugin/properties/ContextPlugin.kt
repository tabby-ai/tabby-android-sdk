package ai.tabby.android.internal.analytics.impl.plugin.properties

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent
import ai.tabby.android.internal.analytics.impl.ContextProperties
import ai.tabby.android.internal.analytics.impl.Parameters
import ai.tabby.android.internal.analytics.impl.apply
import ai.tabby.android.internal.analytics.impl.plugin.Plugin

private object ContextPlugin : Plugin {
    private const val CONTEXT_KEY: String = "context"

    @Suppress("UNCHECKED_CAST")
    override fun apply(event: AnalyticsEvent, params: ContextProperties) {
        val eventContext: Parameters = event.getOrElse(CONTEXT_KEY) {
            emptyMap<String, Any>()
        } as? Parameters ?: emptyMap()

        val parameters = mapOf(
            CONTEXT_KEY to eventContext.applyContext(params)
        )

        event.apply(parameters)
    }
}

private fun ContextProperties.applyContext(properties: ContextProperties): ContextProperties {
    return toMutableMap().apply {
        putAll(properties)
    }
}

internal fun AnalyticsEvent.applyContextProperties(properties: ContextProperties) {
    ContextPlugin.apply(this, properties)
}