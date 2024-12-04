package ai.tabby.android.internal.logger

import android.util.Log
import javax.inject.Inject

internal class TabbyLoggerImpl @Inject constructor() : TabbyLogger {

    companion object {
        private const val TAG_PREFIX = "Tby"
    }

    override fun i(tag: String, message: () -> String) {
        Log.i(TAG_PREFIX + tag, message())
    }

    override fun v(tag: String, message: () -> String) {
        Log.v(TAG_PREFIX + tag, message())
    }

    override fun w(tag: String, throwable: Throwable?, message: () -> String) {
        throwable?.let { Log.w(TAG_PREFIX + tag, message(), it) }
            ?: Log.w(TAG_PREFIX + tag, message())
    }

    override fun e(tag: String, throwable: Throwable?, message: () -> String) {
        throwable?.let { Log.e(TAG_PREFIX + tag, message(), it) }
            ?: Log.e(TAG_PREFIX + tag, message())
    }
}