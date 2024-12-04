package ai.tabby.android.internal.analytics.impl.di

import ai.tabby.android.internal.analytics.api.SegmentAnalytics
import ai.tabby.android.internal.analytics.impl.SegmentAnalyticsImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal interface AnalyticsModule {

    @Binds
    @Singleton
    fun bindSegmentAnalytics(segmentAnalytics: SegmentAnalyticsImpl): SegmentAnalytics
}