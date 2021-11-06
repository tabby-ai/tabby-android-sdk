package ai.tabby.android.internal.di.module

import ai.tabby.android.internal.logger.TabbyLogger
import ai.tabby.android.internal.network.TabbyService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class NetworkModule {

    @Provides
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
    fun provideTabbyService(
        @Named(BaseModule.BASE_URL_KEY) baseUrl: String,
        okHttpClient: OkHttpClient
    ): TabbyService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TabbyService::class.java)

}