package ai.tabby.android

import ai.tabby.android.internal.di.component.TabbyComponent
import android.content.Context
import java.util.concurrent.atomic.AtomicReference

object TabbyFactory {

    private val tabbyComponentRef = AtomicReference<TabbyComponent?>(null)

    val tabby: Tabby get() =
        tabbyComponentRef.get()?.provideTabby() ?:
            throw NullPointerException("Tabby factory is not initialized! Call initAndGet() first.")

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