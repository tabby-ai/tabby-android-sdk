package ai.tabby.demoapp

import ai.tabby.android.factory.TabbyFactory
import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TabbyFactory.setup(this, "_YOUR_API_KEY_")
    }
}