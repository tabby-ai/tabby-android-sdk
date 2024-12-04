package ai.tabby.android.internal.analytics.impl.network

import ai.tabby.android.internal.analytics.impl.AnalyticsEvent
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AnalyticsService {
    @POST("/v1/t")
    suspend fun track(
        @Body event: AnalyticsEvent,
    )
}