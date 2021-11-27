package ai.tabby.android.internal.di.component

import ai.tabby.android.internal.di.module.BaseModule
import ai.tabby.android.internal.di.module.LoggerModule
import ai.tabby.android.internal.logger.TabbyLogger
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Component(
    modules = [
        BaseModule::class,
        LoggerModule::class,
    ]
)
@Singleton
internal interface BaseComponent {

    @Named(BaseModule.BASE_URL_KEY)
    fun provideBaseUrl(): String

    fun provideLogger(): TabbyLogger
}