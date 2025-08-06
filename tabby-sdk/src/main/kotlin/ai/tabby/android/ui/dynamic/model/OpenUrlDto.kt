package ai.tabby.android.ui.dynamic.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
internal data class OpenUrlDto(
    val url: String,
    val data: String,
)