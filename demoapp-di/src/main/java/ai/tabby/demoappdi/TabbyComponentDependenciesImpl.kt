package ai.tabby.demoappdi

import ai.tabby.android.internal.network.TabbyEnvironment
import ai.tabby.android.di.TabbyComponentDependencies
import android.content.Context

class TabbyComponentDependenciesImpl(
    private val context: Context,
    private val apiKey: String,
    private val environment: TabbyEnvironment
) : TabbyComponentDependencies {
    override fun getContext(): Context = context
    override fun getApiKey(): String = apiKey
    override fun getEnvUrl(): String = environment.value
}
