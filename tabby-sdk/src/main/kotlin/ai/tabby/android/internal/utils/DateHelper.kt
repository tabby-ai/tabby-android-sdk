package ai.tabby.android.internal.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

internal object DateHelper {
    private const val ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    @JvmName("toIsoStringNullable")
    fun Date?.toIsoStringUTC(): String? {
        return this?.toIsoStringUTC()
    }

    fun Date.toIsoStringUTC(): String {
        return this.toIsoString(TimeZone.getTimeZone("UTC"))
    }

    @JvmName("toIsoStringNullable")
    fun Date?.toIsoString(timeZone: TimeZone): String? {
        return this?.toIsoString(timeZone)
    }

    fun Date.toIsoString(timeZone: TimeZone): String {
        return SimpleDateFormat(
            ISO_8601_24H_FULL_FORMAT,
            TabbyLanguageResolver.getNumberLocale()
        ).apply {
            this.timeZone = timeZone
        }.format(this)
    }

    fun now(
        timeZone: TimeZone
    ): String = Date().toIsoString(timeZone)
}