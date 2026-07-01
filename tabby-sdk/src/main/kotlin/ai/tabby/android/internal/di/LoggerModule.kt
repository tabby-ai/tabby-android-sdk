package ai.tabby.android.internal.di

import ai.tabby.android.internal.logger.TabbyLogger
import ai.tabby.android.internal.logger.TabbyLoggerImpl

internal interface LoggerModule {
    val logger: TabbyLogger
}

internal fun LoggerModule() = object : LoggerModule {
    override val logger: TabbyLogger = TabbyLoggerImpl()
}
