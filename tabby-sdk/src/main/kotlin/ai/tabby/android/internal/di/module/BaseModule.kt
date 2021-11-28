package ai.tabby.android.internal.di.module

import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class BaseModule {

    companion object {
        const val BASE_URL_KEY = "baseUrl"
    }

    @Provides
    @Named(BASE_URL_KEY)
    @Singleton
    fun provideBaseUrl() = "https://api.tabby.ai/"
}