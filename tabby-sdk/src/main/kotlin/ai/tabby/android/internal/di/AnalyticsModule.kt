package ai.tabby.android.internal.di

import ai.tabby.android.internal.analytics.api.EventCollectorAnalytics
import ai.tabby.android.internal.analytics.impl.EventCollectorAnalyticsImpl

internal interface AnalyticsModule {
    val eventCollectorAnalytics: EventCollectorAnalytics
}

internal interface AnalyticsModuleDependencies {
    val apiKey: String
}

internal fun AnalyticsModule(
    deps: AnalyticsModuleDependencies,
    loggerModule: LoggerModule,
    networkModule: NetworkModule,
) = object : AnalyticsModule {
    override val eventCollectorAnalytics: EventCollectorAnalytics by lazy {
        EventCollectorAnalyticsImpl(
            service = networkModule.analyticsService,
            logger = loggerModule.logger,
            apiKey = deps.apiKey,
        )
    }
}