package ai.tabby.demoapp

import ai.tabby.android.factory.TabbyFactory
import ai.tabby.android.internal.network.TabbyEnvironment
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            TabbyFactory.setup(this@App, "_YOUR_API_KEY_", TabbyEnvironment.Prod)
        }
    }
}
