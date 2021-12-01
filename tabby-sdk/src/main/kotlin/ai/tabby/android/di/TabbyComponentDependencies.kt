package ai.tabby.android.di

import android.content.Context
import javax.inject.Named

interface TabbyComponentDependencies {

    companion object {
        const val API_KEY_KEY = "apiKey"
    }

    fun getContext(): Context

    @Named(API_KEY_KEY)
    fun getApiKey(): String
}