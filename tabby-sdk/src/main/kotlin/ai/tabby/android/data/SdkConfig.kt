package ai.tabby.android.data

import ai.tabby.android.internal.network.dto.SdkConfigEntryDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SdkEndpoints(
    @SerialName("checkoutApiBaseUrl")
    val checkoutApiBaseUrl: String,
    @SerialName("webCheckoutBaseUrl")
    val webCheckoutBaseUrl: String,
    @SerialName("widgetsBaseUrl")
    val widgetsBaseUrl: String,
    @SerialName("analyticsBaseUrl")
    val analyticsBaseUrl: String,
)

data class SdkConfig(
    private val endpointsByKey: Map<String, SdkEndpoints>,
) {
    fun endpointsFor(currency: Currency): SdkEndpoints =
        endpointsByKey[currency.name] ?: endpointsByKey[GENERAL]
        ?: error("SdkConfig missing '$GENERAL' entry")

    internal companion object {
        fun fromEntries(entries: Map<String, SdkConfigEntryDto>): SdkConfig {
            if (!entries.containsKey(GENERAL))
                throw IllegalArgumentException("Bootstrap config missing '$GENERAL' entry")
            return SdkConfig(entries.mapValues { it.value.endpoints })
        }

        const val GENERAL = "general"
    }
}
