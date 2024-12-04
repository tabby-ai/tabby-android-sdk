package ai.tabby.android.internal.analytics.impl.plugin.properties

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent
import ai.tabby.android.internal.analytics.impl.IntegrationsProperties
import ai.tabby.android.internal.analytics.impl.Parameters
import ai.tabby.android.internal.analytics.impl.apply
import ai.tabby.android.internal.analytics.impl.plugin.Plugin

private object IntegrationsPlugin : Plugin {
    private const val INTEGRATIONS_KEY: String = "integrations"

    @Suppress("UNCHECKED_CAST")
    override fun apply(event: AnalyticsEvent, params: IntegrationsProperties) {
        val eventContext: Parameters = event.getOrElse(INTEGRATIONS_KEY) {
            emptyMap<String, Any>()
        } as? Parameters ?: emptyMap()

        val parameters = mapOf(
            INTEGRATIONS_KEY to eventContext.applyIntegrations(params)
        )

        event.apply(parameters)
    }
}

private fun IntegrationsProperties.applyIntegrations(properties: IntegrationsProperties): IntegrationsProperties {
    return toMutableMap().apply {
        putAll(properties)
    }
}

internal fun AnalyticsEvent.applyIntegrationsProperties(properties: IntegrationsProperties) {
    IntegrationsPlugin.apply(this, properties)
}