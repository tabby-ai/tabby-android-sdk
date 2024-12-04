package ai.tabby.android.internal.analytics.api

import ai.tabby.android.internal.analytics.impl.Parameters

internal sealed interface Event {
    val name: String
    val params: Parameters
    val type: String

    abstract class TrackEvent : Event {
        final override val type: String = "track"
    }
}