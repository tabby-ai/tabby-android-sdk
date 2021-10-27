package ai.tabby.android.internal.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class BaseModule(
    private val context: Context,
    private val apiKey: String,
) {

    companion object {
        const val BASE_URL_KEY = "baseUrl"
        const val API_KEY_KEY = "apiKey"
    }

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    @Named(BASE_URL_KEY)
    fun provideBaseUrl() = "https://api.tabby.ai/"

    @Provides
    @Singleton
    @Named(API_KEY_KEY)
    fun provideApiKey() = apiKey

}