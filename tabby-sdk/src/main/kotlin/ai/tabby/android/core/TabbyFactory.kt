package ai.tabby.android.core

import ai.tabby.android.internal.di.component.DaggerBaseComponent
import ai.tabby.android.internal.di.component.DaggerTabbyComponent
import ai.tabby.android.internal.di.component.TabbyComponent
import ai.tabby.android.internal.di.extdep.TabbyComponentDependencies
import android.content.Context
import android.util.Log
import java.util.concurrent.atomic.AtomicReference

/**
 * Tabby factory provides methods for initialization and access to [Tabby] instance.
 */
object TabbyFactory {

    private val tabbyComponentRef = AtomicReference<TabbyComponent?>(null)

    /**
     * Tabby instance.
     *
     * Factory method [setup] must be called to initialize this property. Otherwise
     * [NullPointerException] will be thrown when accessing it before initialization.
     *
     * @see setup
     */
    val tabby: Tabby
        get() = tabbyComponentRef.get()?.provideTabby()
            ?: throw NullPointerException("Tabby factory is not initialized! Call initAndGet() first.")

    /**
     * Crates and returns [Tabby] instance. Also method stores instance in the [tabby] property.
     *
     * This method must be called once when app is starting, usually in `Application.onCreate()`.
     *
     * You don't need to store [Tabby] instance, because it is always available via
     * [TabbyFactory.tabby] property once `setup()` is called.
     *
     * @param context Application context
     * @param apiKey Your Tabby API key
     * @see tabby
     */
    fun setup(
        context: Context,
        apiKey: String,
    ): Tabby {
        val component = tabbyComponentRef.get()
        if (component != null) {
            Log.e("Tabby", "setup is called more than once")
            return component.provideTabby()
        }
        synchronized(tabbyComponentRef) {
            if (tabbyComponentRef.get() == null) {
                val tabbyDependencies = TabbyComponentDependenciesImpl(
                    context = context,
                    apiKey = apiKey
                )
                val baseComponent = DaggerBaseComponent.builder().build()
                val newComponent = DaggerTabbyComponent.builder()
                    .baseComponent(baseComponent)
                    .tabbyComponentDependencies(tabbyDependencies)
                    .build()
                tabbyComponentRef.compareAndSet(null, newComponent)
            }
        }
        return tabby
    }
}

private class TabbyComponentDependenciesImpl(
    private val context: Context,
    private val apiKey: String
) : TabbyComponentDependencies {
    override fun provideContext(): Context = context
    override fun provideApiKey(): String = apiKey
}
