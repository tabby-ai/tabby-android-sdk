package ai.tabby.android.internal.network

import ai.tabby.android.data.SdkConfig
import ai.tabby.android.data.SdkConfig.Companion.GENERAL
import ai.tabby.android.data.SdkEndpoints

sealed interface TabbyEnvironment {
    data object Stage : TabbyEnvironment

    data object Prod : TabbyEnvironment

    data class Custom(val baseUrl: String, val analyticsUrl: String) : TabbyEnvironment
}

internal val TabbyEnvironment.baseUrl: String
    get() = when (this) {
        is TabbyEnvironment.Stage -> "https://api.tabby.dev/"
        is TabbyEnvironment.Prod -> "https://api.tabby.ai/"
        is TabbyEnvironment.Custom -> baseUrl
    }

internal val TabbyEnvironment.analyticsUrl: String
    get() = when (this) {
        is TabbyEnvironment.Stage -> "https://dp-event-collector.tabby.dev/"
        is TabbyEnvironment.Prod -> "https://dp-event-collector.tabby.ai/"
        is TabbyEnvironment.Custom -> analyticsUrl
    }

internal val TabbyEnvironment.defaultSdkConfig: SdkConfig
    get() = SdkConfig(
        buildMap {
            put(
                GENERAL,
                when (this@defaultSdkConfig) {
                    is TabbyEnvironment.Custom,
                    is TabbyEnvironment.Stage -> SdkEndpoints(
                        checkoutApiBaseUrl = "https://api.tabby.dev",
                        webCheckoutBaseUrl = "https://checkout.tabby.dev",
                        widgetsBaseUrl = "https://widgets.tabby.dev",
                        analyticsBaseUrl = "https://dp-event-collector.tabby.dev",
                    )

                    is TabbyEnvironment.Prod -> SdkEndpoints(
                        checkoutApiBaseUrl = "https://api.tabby.ai",
                        webCheckoutBaseUrl = "https://checkout.tabby.ai",
                        widgetsBaseUrl = "https://widgets.tabby.ai",
                        analyticsBaseUrl = "https://dp-event-collector.tabby.ai",
                    )
                }
            )
        }
    )
