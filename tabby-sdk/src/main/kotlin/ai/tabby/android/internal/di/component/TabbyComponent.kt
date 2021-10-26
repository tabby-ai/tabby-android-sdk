package ai.tabby.android.internal.di.component

import ai.tabby.android.Tabby
import ai.tabby.android.internal.di.module.TabbyModule
import android.content.Context
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class TabbyScope

@Component(
    modules = [
        TabbyModule::class
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
            baseUrl: String,
            merchantId: String,
        ): TabbyComponent {
            val baseComponent = BaseComponent.create(
                context = context,
                baseUrl = baseUrl,
                merchantId = merchantId
            )
            return DaggerTabbyComponent.builder()
                .baseComponent(baseComponent)
                .build()
        }
    }

    fun provideTabby(): Tabby
}