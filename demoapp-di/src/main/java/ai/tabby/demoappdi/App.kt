package ai.tabby.demoappdi

import ai.tabby.android.factory.TabbyFactory
import ai.tabby.android.internal.network.TabbyEnvironment
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    lateinit var myTabbyComponent: MyTabbyComponent

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            val tabbyComponent = TabbyFactory.setupTabbyComponent(
                this@App,
                "_YOUR_API_KEY_",
                TabbyEnvironment.Prod,
            )
            myTabbyComponent = DaggerMyTabbyComponent.builder()
                .tabbyComponent(tabbyComponent)
                .build()
        }
    }
}
