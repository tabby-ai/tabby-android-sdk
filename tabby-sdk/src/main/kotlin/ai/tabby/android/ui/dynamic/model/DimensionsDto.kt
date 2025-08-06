package ai.tabby.android.ui.dynamic.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
internal data class DimensionsDto(
    val width: Float,
    val height: Float,
)