package ai.tabby.android.internal.analytics.impl

import ai.tabby.android.di.TabbyComponentDependencies
import ai.tabby.android.internal.analytics.api.Event
import ai.tabby.android.internal.analytics.api.SegmentAnalytics
import ai.tabby.android.internal.analytics.impl.network.AnalyticsService
import ai.tabby.android.internal.analytics.impl.plugin.BasicInfoPlugin
import ai.tabby.android.internal.analytics.impl.plugin.EventContextPlugin
import ai.tabby.android.internal.analytics.impl.plugin.EventIdPlugin
import ai.tabby.android.internal.analytics.impl.plugin.IntegrationsPlugin
import ai.tabby.android.internal.analytics.impl.plugin.InternalPropertiesPlugin
import ai.tabby.android.internal.analytics.impl.plugin.Plugin
import ai.tabby.android.internal.analytics.impl.plugin.properties.applyProperties
import ai.tabby.android.internal.logger.TabbyLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

internal class SegmentAnalyticsImpl @Inject constructor(
    private val service: AnalyticsService,
    private val logger: TabbyLogger,
    @Named(TabbyComponentDependencies.API_KEY_KEY)
    private val apiKey: String,
) : SegmentAnalytics, CoroutineScope {

    companion object {
        private const val TAG: String = "TabbyAnalytics"
    }

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, ex ->
            onError(ex)
        }

    private val plugins: List<Plugin> = listOf(
        BasicInfoPlugin,
        EventIdPlugin,
        IntegrationsPlugin,
        EventContextPlugin,
        InternalPropertiesPlugin(apiKey)
    )

    private val actor = MutableSharedFlow<Event>(replay = 1).apply {
        onEach {
            this@SegmentAnalyticsImpl.launch {
                try {
                    handleEvent(it)
                } catch (ex: Throwable) {
                    onError(ex)
                }
            }
        }.launchIn(this@SegmentAnalyticsImpl)
    }

    override fun sendEvent(event: Event) {
        actor.tryEmit(event)
    }

    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.TrackEvent -> service.track(event.createAnalyticsEvent())
        }
    }

    private fun onError(ex: Throwable) {
        if (ex !is CancellationException) {
            logger.e(TAG, ex) { "Tabby analytics exception" }
        }
    }

    private fun Event.createAnalyticsEvent(): AnalyticsEvent {
        return AnalyticsEvent().apply {
            applyPlugins()
            apply(this@createAnalyticsEvent)
            applyProperties(params)
        }
    }

    private fun AnalyticsEvent.applyPlugins() {
        plugins.forEach { it.apply(this) }
    }

}