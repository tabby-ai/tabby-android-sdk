package ai.tabby.android.internal.di

internal object TabbySdkInjector {

    @Volatile
    var component: TabbyInternalComponent? = null
        private set

    fun newInstance(component: TabbyInternalComponent): TabbyInternalComponent {
        this.component = component
        return component
    }
}