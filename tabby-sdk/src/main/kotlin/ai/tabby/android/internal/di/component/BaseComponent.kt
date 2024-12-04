package ai.tabby.android.internal.di.component

import ai.tabby.android.internal.di.module.LoggerModule
import ai.tabby.android.internal.logger.TabbyLogger
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        LoggerModule::class,
    ]
)
@Singleton
internal interface BaseComponent {
    fun provideLogger(): TabbyLogger
}