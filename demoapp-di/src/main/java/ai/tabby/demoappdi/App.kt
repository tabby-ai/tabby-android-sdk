package ai.tabby.demoappdi

import ai.tabby.android.di.DaggerTabbyComponent
import ai.tabby.android.internal.di.component.DaggerBaseComponent
import android.app.Application

class App : Application() {

    val myTabbyComponent: MyTabbyComponent

    init {
        // Tabby setup
        val tabbyDependencies = TabbyComponentDependenciesImpl(
            context = this,
            apiKey = "pk_test_1ff3d031-6346-4456-aa62-01e302499577"
        )

        val baseComponent = DaggerBaseComponent.builder().build()
        val tabbyComponent = DaggerTabbyComponent.builder()
            .baseComponent(baseComponent)
            .tabbyComponentDependencies(tabbyDependencies)
            .build()
        myTabbyComponent = DaggerMyTabbyComponent.builder()
            .tabbyComponent(tabbyComponent)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
    }
}