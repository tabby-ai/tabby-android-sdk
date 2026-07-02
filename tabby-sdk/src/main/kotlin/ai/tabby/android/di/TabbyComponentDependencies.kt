package ai.tabby.android.di

import ai.tabby.android.internal.network.TabbyEnvironment
import android.content.Context

internal interface TabbyComponentDependencies {

    val context: Context

    val environment: TabbyEnvironment

    val apiKey: String
}