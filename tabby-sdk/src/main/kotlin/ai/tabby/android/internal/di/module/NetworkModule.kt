package ai.tabby.android.internal.di.module

import ai.tabby.android.di.TabbyScope
import ai.tabby.android.internal.logger.TabbyLogger
import ai.tabby.android.internal.network.TabbyEnvironment
import ai.tabby.android.internal.network.TabbyService
import ai.tabby.android.internal.network.baseUrl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
internal class NetworkModule {

    @Provides
    @TabbyScope
    fun provideOkHttpClient(
        logger: TabbyLogger
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor {
            logger.v("Net") { it }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @TabbyScope
    fun provideTabbyService(
        environment: TabbyEnvironment,
        okHttpClient: OkHttpClient,
    ): TabbyService = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(environment.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TabbyService::class.java)

}