package ai.tabby.android.internal.network

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