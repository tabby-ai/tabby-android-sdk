package ai.tabby.android.internal.di.module

import ai.tabby.android.Tabby
import ai.tabby.android.internal.core.TabbyImpl
import dagger.Binds
import dagger.Module

@Module
internal interface TabbyModule {

    @Binds
    fun bindTabby(impl: TabbyImpl): Tabby

}