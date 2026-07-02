package ai.tabby.android.internal.di

import ai.tabby.android.internal.analytics.impl.network.AnalyticsHeadersInterceptor
import ai.tabby.android.internal.analytics.impl.network.AnalyticsService
import ai.tabby.android.internal.network.SdkConfigService
import ai.tabby.android.internal.network.TabbyEnvironment
import ai.tabby.android.internal.network.TabbyService
import ai.tabby.android.internal.network.analyticsUrl
import ai.tabby.android.internal.network.baseUrl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal interface NetworkModule {
    val tabbyService: TabbyService
    val analyticsService: AnalyticsService
    val sdkConfigService: SdkConfigService
    val json: Json
}

internal interface NetworkModuleDependencies {
    val environment: TabbyEnvironment
}

internal fun NetworkModule(
    deps: NetworkModuleDependencies,
    loggerModule: LoggerModule,
): NetworkModule {
    return object : NetworkModule {

        private val logger = loggerModule.logger

        private val environment = deps.environment

        override val json: Json = Json { ignoreUnknownKeys = true }

        override val sdkConfigService: SdkConfigService by lazy {
            Retrofit.Builder()
                .baseUrl(environment.baseUrl)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(SdkConfigService::class.java)
        }

        override val analyticsService: AnalyticsService by lazy {
            Retrofit.Builder()
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(
                            HttpLoggingInterceptor {
                                logger.v("TabbyAnalytics") { it }
                            }.apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            }
                        ).addInterceptor(AnalyticsHeadersInterceptor).build()
                )
                .baseUrl(environment.analyticsUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AnalyticsService::class.java)
        }

        override val tabbyService: TabbyService by lazy {
            Retrofit.Builder()
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor { logger.v("Net") { it } }.apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                        .build()
                )
                .baseUrl(environment.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TabbyService::class.java)
        }
    }
}