package ai.tabby.android.factory

import ai.tabby.android.core.Tabby
import ai.tabby.android.di.TabbyComponent
import ai.tabby.android.di.TabbyComponentDependencies
import ai.tabby.android.factory.TabbyFactory.setup
import ai.tabby.android.factory.TabbyFactory.tabby
import ai.tabby.android.internal.di.TabbyInternalComponent
import ai.tabby.android.internal.di.TabbyInternalComponentDependencies
import ai.tabby.android.internal.di.TabbySdkInjector
import ai.tabby.android.internal.network.TabbyEnvironment
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first

/**
 * Tabby factory provides methods for initialization and access to [Tabby] instance.
 */
object TabbyFactory {

    private val tabbyComponentState = MutableSharedFlow<TabbyComponent>(replay = 1)

    internal val tabbyComponentFlow: SharedFlow<TabbyComponent>
        get() = tabbyComponentState.asSharedFlow()

    /**
     * Returns the Tabby instance if it has been initialized, otherwise null.
     */
    fun tabbyOrNull(): Tabby? = TabbySdkInjector.component?.tabbyComponent?.tabby

    /**
     * Tabby instance. [setup] must be called and awaited before accessing this property.
     */
    suspend fun tabby(): Tabby = awaitComponent().tabby

    /**
     * Initialises the SDK by fetching sharded endpoint configuration and creating the Tabby component.
     *
     * Must be called once before accessing [tabby]. Suspend — call from a coroutine scope.
     * If called more than once the existing instance is returned without re-fetching.
     */
    suspend fun setup(
        context: Context,
        apiKey: String,
        environment: TabbyEnvironment,
    ): Tabby = setupTabbyComponent(context, apiKey, environment).tabby

    suspend fun setupTabbyComponent(
        context: Context,
        apiKey: String,
        environment: TabbyEnvironment,
    ): TabbyComponent {
        val existing = TabbySdkInjector.component
        if (existing != null) {
            Log.w("Tabby", "setup is called more than once")
            return existing.tabbyComponent!!
        }

        val internalDependencies = object : TabbyInternalComponentDependencies {
            override val environment: TabbyEnvironment = environment
            override val apiKey: String = apiKey
        }
        val internalComponent = TabbyInternalComponent(internalDependencies)
        TabbySdkInjector.newInstance(internalComponent)

        val dependencies = object : TabbyComponentDependencies {
            override val context: Context = context
            override val environment: TabbyEnvironment = environment
            override val apiKey: String = apiKey
        }
        val component = internalComponent.tabbyComponent(dependencies)
        tabbyComponentState.emit(component)

        return component
    }

    private suspend fun awaitComponent(): TabbyComponent = tabbyComponentState.first()
}
