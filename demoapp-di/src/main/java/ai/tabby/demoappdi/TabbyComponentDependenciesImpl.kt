package ai.tabby.demoappdi

import ai.tabby.android.di.TabbyComponentDependencies
import android.content.Context

class TabbyComponentDependenciesImpl(
    private val context: Context,
    private val apiKey: String
) : TabbyComponentDependencies {
    override fun getContext(): Context = context
    override fun getApiKey(): String = apiKey
}
