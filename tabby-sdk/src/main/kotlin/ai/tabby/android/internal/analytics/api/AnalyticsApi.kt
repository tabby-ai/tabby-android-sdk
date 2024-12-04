package ai.tabby.android.internal.analytics.api

import ai.tabby.android.internal.di.Api
import ai.tabby.android.internal.di.api

internal interface AnalyticsApi : Api {
    val segmentAnalytics: SegmentAnalytics
}

internal val analyticsApi: AnalyticsApi by api()