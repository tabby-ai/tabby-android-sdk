package ai.tabby.android.internal.di.module

import ai.tabby.android.Tabby
import ai.tabby.android.internal.core.TabbyImpl
import ai.tabby.android.internal.network.TabbyService
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

@Module
internal class TabbyModule {

    @Provides
    fun provideTabby(
        @Named(BaseModule.API_KEY_KEY) apiKey: String,
        tabbyService: TabbyService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): Tabby =
        TabbyImpl(
            apiKey = apiKey,
            tabbyService = tabbyService,
            ioDispatcher = ioDispatcher
        )

}