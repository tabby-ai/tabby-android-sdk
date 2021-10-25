package ai.tabby.android.internal.di.component

import ai.tabby.android.internal.di.module.BaseModule
import android.content.Context
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Component(
    modules = [
        BaseModule::class
    ]
)
@Singleton
internal interface BaseComponent {

    companion object {

        fun create(
            context: Context,
            baseUrl: String,
            merchantId: String,
        ): BaseComponent = DaggerBaseComponent.builder()
            .baseModule(
                BaseModule(
                    context = context,
                    baseUrl = baseUrl,
                    merchantId = merchantId
                )
            )
            .build()

    }

    @Named(BaseModule.BASE_URL_KEY)
    fun provideBaseUrl(): String
}