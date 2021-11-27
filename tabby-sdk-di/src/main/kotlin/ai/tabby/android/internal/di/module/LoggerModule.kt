package ai.tabby.android.internal.di.module

import ai.tabby.android.internal.logger.TabbyLogger
import ai.tabby.android.internal.logger.TabbyLoggerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal interface LoggerModule {

    @Binds
    @Singleton
    fun bindLogger(loggerImpl: TabbyLoggerImpl): TabbyLogger
}