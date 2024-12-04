package ai.tabby.android.internal.di.module

import ai.tabby.android.core.Tabby
import ai.tabby.android.di.TabbyComponentDependencies
import ai.tabby.android.di.TabbyScope
import ai.tabby.android.internal.core.TabbyImpl
import ai.tabby.android.internal.logger.TabbyLogger
import ai.tabby.android.internal.network.TabbyEnvironment
import ai.tabby.android.internal.network.TabbyService
import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

@Module
internal class TabbyModule {

    @Provides
    @TabbyScope
    fun provideTabby(
        context: Context,
        @Named(TabbyComponentDependencies.API_KEY_KEY) apiKey: String,
        environment: TabbyEnvironment,
        tabbyService: TabbyService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        logger: TabbyLogger
    ): Tabby =
        TabbyImpl(
            context = context,
            apiKey = apiKey,
            environment = environment,
            tabbyService = tabbyService,
            ioDispatcher = ioDispatcher,
            logger = logger
        )
}