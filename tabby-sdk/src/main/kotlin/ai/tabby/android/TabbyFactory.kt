package ai.tabby.android

import ai.tabby.android.internal.di.component.TabbyComponent
import android.content.Context
import java.util.concurrent.atomic.AtomicReference

object TabbyFactory {

    private val tabbyComponentRef = AtomicReference<TabbyComponent?>(null)

    val tabby: Tabby get() =
        tabbyComponentRef.get()?.provideTabby() ?:
            throw NullPointerException("Tabby factory is not initialized! Call initAndGet() first.")

    fun initAndGet(
        context: Context,
        baseUrl: String,
        merchantId: String,
    ): Tabby {
        val component = tabbyComponentRef.get()
        if (component != null) {
            return component.provideTabby()
        }
        synchronized(tabbyComponentRef) {
            if (tabbyComponentRef.get() == null) {
                val newComponent = TabbyComponent.create(
                    context = context,
                    baseUrl = baseUrl,
                    merchantId = merchantId,
                )
                tabbyComponentRef.set(newComponent)
            }
        }
        return tabby
    }
}