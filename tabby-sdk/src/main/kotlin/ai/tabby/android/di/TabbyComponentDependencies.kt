package ai.tabby.android.di

import ai.tabby.android.internal.network.TabbyEnvironment
import android.content.Context
import javax.inject.Named

interface TabbyComponentDependencies {

    companion object {
        const val API_KEY_KEY = "apiKey"
    }

    fun getContext(): Context

    fun getEnv(): TabbyEnvironment

    @Named(API_KEY_KEY)
    fun getApiKey(): String
}