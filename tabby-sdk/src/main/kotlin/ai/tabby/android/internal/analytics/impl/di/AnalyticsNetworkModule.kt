package ai.tabby.android.internal.analytics.impl.di

import ai.tabby.android.internal.analytics.impl.network.AnalyticsHeadersInterceptor
import ai.tabby.android.internal.analytics.impl.network.AnalyticsService
import ai.tabby.android.internal.logger.TabbyLogger
import ai.tabby.android.internal.network.TabbyEnvironment
import ai.tabby.android.internal.network.analyticsUrl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class AnalyticsNetworkModule {

    @Provides
    @Named("AnalyticsClient")
    @Singleton
    fun provideOkHttpClient(
        logger: TabbyLogger,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor {
            logger.v("TabbyAnalytics") { it }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AnalyticsHeadersInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAnalyticsService(
        environment: TabbyEnvironment,
        @Named("AnalyticsClient")
        okHttpClient: OkHttpClient,
    ): AnalyticsService = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(environment.analyticsUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AnalyticsService::class.java)

}