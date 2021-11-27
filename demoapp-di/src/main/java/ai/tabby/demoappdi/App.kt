package ai.tabby.demoappdi

import ai.tabby.android.di.TabbyComponent
import android.app.Application

class App : Application() {

    val myTabbyComponent: MyTabbyComponent

    init {
        // Tabby setup
        val tabbyDependencies = TabbyComponentDependenciesImpl(
            context = this, // application context
            apiKey = "pk_test_1ff3d031-6346-4456-aa62-01e302499577"
        )

        // Create tabby component and link it to the injectable app's component
        val tabbyComponent = TabbyComponent.create(dependencies = tabbyDependencies)
        myTabbyComponent = DaggerMyTabbyComponent.builder()
            .tabbyComponent(tabbyComponent)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
    }
}