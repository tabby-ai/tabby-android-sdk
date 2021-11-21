package ai.tabby.demoapp

import ai.tabby.android.core.TabbyFactory
import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TabbyFactory.setup(this, "pk_test_1ff3d031-6346-4456-aa62-01e302499577")
    }
}