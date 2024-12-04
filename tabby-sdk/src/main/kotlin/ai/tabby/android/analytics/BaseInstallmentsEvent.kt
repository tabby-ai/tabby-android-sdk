package ai.tabby.android.analytics

import ai.tabby.android.internal.analytics.api.Event
import ai.tabby.android.internal.analytics.impl.Parameters
import ai.tabby.android.internal.analytics.impl.plugin.properties.applyProperties

internal abstract class BaseInstallmentsEvent : Event.TrackEvent() {

    protected abstract val installmentsCount: Int
    protected abstract val countryName: String

    final override val params: Parameters
        get() = buildParams().applyProperties(
            mapOf(
                "platformType" to "merchant app",
                "productType" to "installments",
                "merchantIntegrationType" to "snippetAndPopup",
                "popupType" to "standardWithInfo",
                "snippetType" to "fullInformation",
            )
        )

    private fun buildParams(): Parameters {
        return mapOf(
            "planSelected" to installmentsCount,
            "merchantCountry" to countryName,
        )
    }
}