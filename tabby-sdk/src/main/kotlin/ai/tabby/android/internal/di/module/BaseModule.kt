package ai.tabby.android.internal.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class BaseModule(
    private val context: Context,
    private val baseUrl: String,
    private val merchantId: String,
) {

    companion object {
        const val BASE_URL_KEY = "baseUrl"
        const val MERCHANT_ID_KEY = "merchantId"
    }

    @Provides
    fun provideContext() = context

    @Provides
    @Named(BASE_URL_KEY)
    fun provideBaseUrl() = baseUrl

    @Provides
    @Named(MERCHANT_ID_KEY)
    fun provideMerchantId() = merchantId

}