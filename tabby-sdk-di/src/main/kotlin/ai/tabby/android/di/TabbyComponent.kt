package ai.tabby.android.di

import ai.tabby.android.core.Tabby
import ai.tabby.android.internal.di.component.BaseComponent
import ai.tabby.android.internal.di.component.DaggerBaseComponent
import ai.tabby.android.internal.di.module.DispatcherModule
import ai.tabby.android.internal.di.module.NetworkModule
import ai.tabby.android.internal.di.module.TabbyModule
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

    companion object {

        fun create(
            dependencies: TabbyComponentDependencies
        ): TabbyComponent =
            DaggerTabbyComponent.builder()
                .baseComponent(DaggerBaseComponent.create())
                .tabbyComponentDependencies(dependencies)
                .build()
    }
}