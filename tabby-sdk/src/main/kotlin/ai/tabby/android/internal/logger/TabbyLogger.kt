package ai.tabby.android.internal.logger

internal interface TabbyLogger {
    fun logI(tag: String, message: () -> String)
    fun logV(tag: String, message: () -> String)
    fun logW(tag: String, throwable: Throwable? = null, message: () -> String)
    fun logE(tag: String, throwable: Throwable? = null, message: () -> String)
}