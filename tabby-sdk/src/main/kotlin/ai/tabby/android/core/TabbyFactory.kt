package ai.tabby.android.core

import ai.tabby.android.internal.di.component.TabbyComponent
import android.content.Context
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
        get() =
        tabbyComponentRef.get()?.provideTabby() ?:
            throw NullPointerException("Tabby factory is not initialized! Call initAndGet() first.")

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
            return component.provideTabby()
        }
        synchronized(tabbyComponentRef) {
            if (tabbyComponentRef.get() == null) {
                val newComponent = TabbyComponent.create(
                    context = context,
                    apiKey = apiKey,
                )
                tabbyComponentRef.set(newComponent)
            }
        }
        return tabby
    }
}