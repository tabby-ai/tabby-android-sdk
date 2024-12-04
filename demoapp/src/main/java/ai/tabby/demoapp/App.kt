package ai.tabby.demoapp

import ai.tabby.android.factory.TabbyFactory
import ai.tabby.android.internal.network.TabbyEnvironment
import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TabbyFactory.setup(this, "_YOUR_API_KEY_", TabbyEnvironment.Prod)
    }
}