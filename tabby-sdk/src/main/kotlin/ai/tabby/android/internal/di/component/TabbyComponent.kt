package ai.tabby.android.internal.di.component

import ai.tabby.android.core.Tabby
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
        BaseComponent::class
    ]
)
@TabbyScope
internal interface TabbyComponent {

    companion object {

        fun create(
            context: Context,
            apiKey: String,
        ): TabbyComponent {
            val baseComponent = BaseComponent.create(
                context = context,
                apiKey = apiKey
            )
            return DaggerTabbyComponent.builder()
                .baseComponent(baseComponent)
                .build()
        }
    }

    fun provideTabby(): Tabby

    fun provideLogger(): TabbyLogger
}