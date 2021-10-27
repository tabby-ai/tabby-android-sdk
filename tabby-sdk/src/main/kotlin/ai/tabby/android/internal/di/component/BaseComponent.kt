package ai.tabby.android.internal.di.component

import ai.tabby.android.internal.di.module.BaseModule
import ai.tabby.android.internal.di.module.LoggerModule
import ai.tabby.android.internal.logger.TabbyLogger
import android.content.Context
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

    companion object {

        fun create(
            context: Context,
            apiKey: String,
        ): BaseComponent = DaggerBaseComponent.builder()
            .baseModule(
                BaseModule(
                    context = context,
                    apiKey = apiKey
                )
            )
            .build()

    }

    fun provideContext(): Context

    @Named(BaseModule.BASE_URL_KEY)
    fun provideBaseUrl(): String

    @Named(BaseModule.API_KEY_KEY)
    fun provideApiKey(): String

    fun provideLogger(): TabbyLogger

}