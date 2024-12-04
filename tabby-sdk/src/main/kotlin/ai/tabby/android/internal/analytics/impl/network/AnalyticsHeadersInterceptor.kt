package ai.tabby.android.internal.analytics.impl.network

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal object AnalyticsHeadersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder: Request.Builder = chain.request().newBuilder()
        requestBuilder.header("Content-Type", "application/json")
        requestBuilder.header("Accept", "application/json")
        requestBuilder.header("X-SDK-Version", "Android ${Build.VERSION.SDK_INT}")
        requestBuilder.header("Authorization", "Basic ${getHeader()}")
        return chain.proceed(requestBuilder.build())
    }

    private fun getHeader(): String {
        val keys = listOf<Long>(
            211107522,
            19250441,
            4221728603,
            1734207083,
            1749337480,
            3381531300,
            2409995332,
            1407431640,
            3379185532,
            3359823664,
            2924734791,
            1581389183,
            1874447321,
            1890879897,
            2070795402,
            334059111,
            3483253102,
            1334540772,
            2293133705,
            71628902,
            3976410035,
            2789056350,
            3208175317,
            2194866016,
            1144840874,
            3728036734,
            1508593681,
            3453517323,
            3170499193,
            1312139652,
            1069259434,
            2342810021,
            4286240038,
            3834145722,
            720980016,
            3237697416,
            2200391240,
            1101136623,
            344693430,
            2385175934,
            4161839652,
            1299262828,
            3022544371,
            1518468162,
        )
        val data = listOf<Long>(
            211107494,
            19250525,
            4221728541,
            1734207009,
            1749337579,
            3381531361,
            2409995286,
            1407431569,
            3379185438,
            3359823736,
            2924734750,
            1581389135,
            1874447290,
            1890879948,
            2070795456,
            334059090,
            3483253055,
            1334540723,
            2293133772,
            71628884,
            3976410081,
            2789056307,
            3208175232,
            2194866005,
            1144840947,
            3728036628,
            1508593756,
            3453517369,
            3170499096,
            1312139752,
            1069259504,
            2342810086,
            4286240107,
            3834145675,
            720980094,
            3237697508,
            2200391194,
            1101136552,
            344693472,
            2385175849,
            4161839734,
            1299262813,
            3022544314,
            1518468212
        )

        val chars = keys.mapIndexed { index, value ->
            value xor data[index]
        }.map { it.toByte() }.toByteArray()

        return String(chars)
    }
}