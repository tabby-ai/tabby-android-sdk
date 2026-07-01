package ai.tabby.android.di

import ai.tabby.android.core.Tabby
import ai.tabby.android.data.SdkConfig

interface TabbyComponent {

    val apiKey: String

    val sdkConfig: SdkConfig

    val tabby: Tabby
}
