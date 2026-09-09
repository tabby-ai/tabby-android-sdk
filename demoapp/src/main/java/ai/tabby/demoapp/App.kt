package ai.tabby.demoapp

import ai.tabby.android.factory.TabbyFactory
import ai.tabby.demoapp.qa.DemoConfig
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DemoConfig.init(this)
        CoroutineScope(Dispatchers.IO).launch {
            TabbyFactory.setup(this@App, DemoConfig.apiKey, DemoConfig.environment)
        }
    }
}
