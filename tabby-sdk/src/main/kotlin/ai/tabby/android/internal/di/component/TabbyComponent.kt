package ai.tabby.android.internal.di.component

import ai.tabby.android.core.Tabby
import ai.tabby.android.internal.di.extdep.TabbyComponentDependencies
import ai.tabby.android.internal.di.module.DispatcherModule
import ai.tabby.android.internal.di.module.NetworkModule
import ai.tabby.android.internal.di.module.TabbyModule
import ai.tabby.android.internal.di.scope.TabbyScope
import ai.tabby.android.internal.logger.TabbyLogger
import android.content.Context
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
internal interface TabbyComponent {

    fun provideTabby(): Tabby

    fun provideLogger(): TabbyLogger
}