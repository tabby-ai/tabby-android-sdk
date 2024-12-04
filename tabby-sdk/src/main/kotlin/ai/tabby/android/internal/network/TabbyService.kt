package ai.tabby.android.internal.network

import ai.tabby.android.internal.network.dto.CheckoutPayloadDto
import ai.tabby.android.internal.network.dto.CheckoutSessionDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

internal interface TabbyService {

    @POST
    suspend fun createSession(
        @Header("Authorization") apiKey: String,
        @Body request: CheckoutPayloadDto,
        @Url envUrl: String
    ): CheckoutSessionDto

}
