package ai.tabby.android.internal.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

internal interface TabbyService {

    @POST("api/v2/checkout/")
    suspend fun createSession(
        @Header("Authorization") apiKey: String,
        @Body request: CheckoutPayloadDto
    ): CheckoutSessionDto

}
