package ai.tabby.demoappdi

import ai.tabby.android.core.Tabby
import ai.tabby.android.di.TabbyComponent
import dagger.Component
import javax.inject.Scope
import javax.inject.Singleton

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Component(
    dependencies = [
        TabbyComponent::class
    ]
)
@AppScope
interface MyTabbyComponent {

    fun provideTabby(): Tabby

    fun inject(vm: CheckoutViewModel)
}
