package ai.tabby.android.internal.logger

internal interface TabbyLogger {
    fun i(tag: String, message: () -> String)
    fun v(tag: String, message: () -> String)
    fun w(tag: String, throwable: Throwable? = null, message: () -> String)
    fun e(tag: String, throwable: Throwable? = null, message: () -> String)
}