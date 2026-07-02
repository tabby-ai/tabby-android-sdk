package ai.tabby.android.internal.network.dto

import ai.tabby.android.data.SdkEndpoints
import kotlinx.serialization.Serializable

@Serializable
internal data class SdkConfigEntryDto(
    val endpoints: SdkEndpoints,
)
