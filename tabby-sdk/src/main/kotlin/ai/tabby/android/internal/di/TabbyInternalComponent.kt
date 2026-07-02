package ai.tabby.android.internal.di

import ai.tabby.android.data.SdkConfig
import ai.tabby.android.di.TabbyComponent
import ai.tabby.android.di.TabbyComponentDependencies
import ai.tabby.android.internal.network.TabbyEnvironment
import ai.tabby.android.internal.network.defaultSdkConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface TabbyInternalComponent :
    NetworkModule,
    AnalyticsModule,
    LoggerModule,
    TabbyInternalComponentDependencies {

    val tabbyComponent: TabbyComponent?

    suspend fun tabbyComponent(deps: TabbyComponentDependencies): TabbyComponent
}

internal interface TabbyInternalComponentDependencies :
    NetworkModuleDependencies,
    AnalyticsModuleDependencies {

    override val apiKey: String

    override val environment: TabbyEnvironment
}

internal fun TabbyInternalComponent(
    deps: TabbyInternalComponentDependencies,
): TabbyInternalComponent {
    val loggerModule = LoggerModule()
    val networkModule = NetworkModule(deps, loggerModule)
    val analyticsModule = AnalyticsModule(deps, loggerModule, networkModule)

    return TabbyInternalComponentImpl(
        deps = deps,
        networkModule = networkModule,
        analyticsModule = analyticsModule,
        loggerModule = loggerModule,
    )
}

private class TabbyInternalComponentImpl(
    deps: TabbyInternalComponentDependencies,
    networkModule: NetworkModule,
    analyticsModule: AnalyticsModule,
    loggerModule: LoggerModule,
) : TabbyInternalComponent,
    TabbyInternalComponentDependencies by deps,
    NetworkModule by networkModule,
    AnalyticsModule by analyticsModule,
    LoggerModule by loggerModule {

    override var tabbyComponent: TabbyComponent? = null

    override suspend fun tabbyComponent(
        deps: TabbyComponentDependencies,
    ): TabbyComponent {
        var component = tabbyComponent
        if (component == null) {
            val sdkConfig = fetchSdkConfig()
            component = TabbyComponentImpl(
                deps = deps,
                sdkConfig = sdkConfig,
                logger = logger,
                tabbyService = tabbyService,
            )
            tabbyComponent = component
            return component
        }
        return component
    }

    private suspend fun fetchSdkConfig(): SdkConfig {
        return withContext(Dispatchers.IO) {
            try {
                val entries = sdkConfigService.getSdkConfig()
                SdkConfig.fromEntries(entries)
            } catch (e: Exception) {
                logger.e("Tabby", e) {
                    "Failed to fetch SDK config"
                }
                environment.defaultSdkConfig
            }
        }
    }
}