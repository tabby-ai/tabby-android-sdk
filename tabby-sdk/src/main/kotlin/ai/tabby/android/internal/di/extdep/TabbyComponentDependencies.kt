package ai.tabby.android.internal.di.extdep

import android.content.Context
import javax.inject.Named

interface TabbyComponentDependencies {

    companion object {
        const val API_KEY_KEY = "apiKey"
    }

    fun provideContext(): Context

    @Named(API_KEY_KEY)
    fun provideApiKey(): String
}