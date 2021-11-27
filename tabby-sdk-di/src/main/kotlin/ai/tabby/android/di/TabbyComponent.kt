package ai.tabby.android.di

import ai.tabby.android.core.Tabby
import ai.tabby.android.internal.di.component.BaseComponent
import ai.tabby.android.internal.di.module.DispatcherModule
import ai.tabby.android.internal.di.module.NetworkModule
import ai.tabby.android.internal.di.module.TabbyModule
import ai.tabby.android.internal.logger.TabbyLogger
import dagger.Component

@Component(
    modules = [
        TabbyModule::class,
        NetworkModule::class,
        DispatcherModule::class,
    ],
    dependencies = [
        BaseComponent::class,
        TabbyComponentDependencies::class
    ]
)
@TabbyScope
interface TabbyComponent {

    fun provideTabby(): Tabby

    fun provideLogger(): TabbyLogger
}