package ai.tabby.android.internal.analytics.api

internal val segmentAnalytics: SegmentAnalytics by lazy { analyticsApi.segmentAnalytics }

internal interface SegmentAnalytics {
    fun sendEvent(event: Event)
}