package ai.tabby.android.ui.dynamic.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
internal data class InitializationDataDto(
    val type: String = "initializationData",
    val data: String?,
)