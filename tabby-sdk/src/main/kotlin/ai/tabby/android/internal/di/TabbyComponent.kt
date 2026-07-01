package ai.tabby.android.internal.di

import ai.tabby.android.core.Tabby
import ai.tabby.android.data.SdkConfig
import ai.tabby.android.di.TabbyComponent
import ai.tabby.android.di.TabbyComponentDependencies
import ai.tabby.android.internal.core.TabbyImpl
import ai.tabby.android.internal.logger.TabbyLogger
import ai.tabby.android.internal.network.TabbyService
import kotlinx.coroutines.Dispatchers

internal class TabbyComponentImpl(
    deps: TabbyComponentDependencies,
    override val sdkConfig: SdkConfig,
    private val logger: TabbyLogger,
    private val tabbyService: TabbyService,
) : TabbyComponent, TabbyComponentDependencies by deps {

    override val tabby: Tabby = TabbyImpl(
        context = context,
        apiKey = apiKey,
        sdkConfig = sdkConfig,
        tabbyService = tabbyService,
        ioDispatcher = Dispatchers.IO,
        logger = logger,
    )
}