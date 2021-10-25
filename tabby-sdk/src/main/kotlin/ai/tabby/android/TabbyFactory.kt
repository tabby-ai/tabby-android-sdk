package ai.tabby.android

import ai.tabby.android.internal.core.TabbyImpl

object TabbyFactory {

    lateinit var tabby: Tabby
        private set

    fun initAndGet(): Tabby {
        if (this::tabby.isInitialized) {
            return tabby
        }
        synchronized(this) {
            if (!this::tabby.isInitialized) {
                tabby = TabbyImpl()
            }
        }
        return tabby
    }
}