package ai.tabby.android.internal.analytics.impl.di

import ai.tabby.android.di.TabbyComponentDependencies
import ai.tabby.android.internal.analytics.api.AnalyticsApi
import ai.tabby.android.internal.di.apiCreator
import ai.tabby.android.internal.di.module.LoggerModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AnalyticsModule::class,
        AnalyticsNetworkModule::class,
        LoggerModule::class
    ],
    dependencies = [
        TabbyComponentDependencies::class,
    ]
)
@Singleton
internal interface AnalyticsComponent : AnalyticsApi {
    companion object {
        fun create(dependencies: TabbyComponentDependencies): AnalyticsApi =
            apiCreator<AnalyticsApi> {
                DaggerAnalyticsComponent.builder()
                    .tabbyComponentDependencies(dependencies)
                    .build()
            }
    }
}