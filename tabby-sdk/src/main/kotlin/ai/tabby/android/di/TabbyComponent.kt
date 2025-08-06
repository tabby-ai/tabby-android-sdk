package ai.tabby.android.di

import ai.tabby.android.core.Tabby
import ai.tabby.android.di.TabbyComponentDependencies.Companion.API_KEY_KEY
import ai.tabby.android.internal.analytics.api.AnalyticsApi
import ai.tabby.android.internal.analytics.impl.di.AnalyticsComponent
import ai.tabby.android.internal.di.component.BaseComponent
import ai.tabby.android.internal.di.component.DaggerBaseComponent
import ai.tabby.android.internal.di.module.DispatcherModule
import ai.tabby.android.internal.di.module.NetworkModule
import ai.tabby.android.internal.di.module.TabbyModule
import ai.tabby.android.internal.network.TabbyEnvironment
import dagger.Component
import javax.inject.Named

@Component(
    modules = [
        TabbyModule::class,
        NetworkModule::class,
        DispatcherModule::class,
    ],
    dependencies = [
        BaseComponent::class,
        TabbyComponentDependencies::class,
        AnalyticsApi::class,
    ]
)
@TabbyScope
interface TabbyComponent {

    @Named(API_KEY_KEY)
    fun apiKey(): String

    fun getEnv(): TabbyEnvironment

    fun provideTabby(): Tabby

    companion object {
        fun create(dependencies: TabbyComponentDependencies): TabbyComponent =
            DaggerTabbyComponent.builder()
                .baseComponent(DaggerBaseComponent.create())
                .tabbyComponentDependencies(dependencies)
                .analyticsApi(AnalyticsComponent.create(dependencies))
                .build()
    }
}