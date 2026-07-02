package ai.tabby.android.internal.network

import ai.tabby.android.internal.network.dto.SdkConfigEntryDto
import retrofit2.http.POST

internal interface SdkConfigService {

    @POST("api/v1/sdk/config")
    suspend fun getSdkConfig(): Map<String, SdkConfigEntryDto>
}
