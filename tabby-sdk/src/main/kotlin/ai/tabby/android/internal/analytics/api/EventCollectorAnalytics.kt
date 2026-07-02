package ai.tabby.android.internal.analytics.api

import ai.tabby.android.internal.di.TabbySdkInjector

internal val eventCollectorAnalytics: EventCollectorAnalytics
    get() = TabbySdkInjector.component?.eventCollectorAnalytics ?: NoOpEventCollectorAnalytics

private object NoOpEventCollectorAnalytics : EventCollectorAnalytics {
    override fun sendEvent(event: Event) = Unit
}

internal interface EventCollectorAnalytics {
    fun sendEvent(event: Event)
}
