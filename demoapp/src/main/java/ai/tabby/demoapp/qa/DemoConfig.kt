package ai.tabby.demoapp.qa

import ai.tabby.android.data.Lang
import ai.tabby.android.internal.network.TabbyEnvironment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * QA-configurable SDK setup, persisted across app restarts.
 *
 * [environment] and [apiKey] are read once in [ai.tabby.demoapp.App.onCreate] before
 * [ai.tabby.android.factory.TabbyFactory.setup] is called. Because `setup` is a no-op on a second
 * call, changing them here only takes effect after the process is restarted (see [restartApp]).
 */
object DemoConfig {

    private const val PREFS_NAME = "demo_config"
    private const val KEY_ENVIRONMENT = "environment"
    private const val KEY_API_KEY = "api_key"
    private const val KEY_CUSTOM_BASE_URL = "custom_base_url"
    private const val KEY_CUSTOM_ANALYTICS_URL = "custom_analytics_url"
    private const val KEY_MERCHANT_CODE = "merchant_code"
    private const val KEY_LANG = "lang"

    private const val ENV_STAGE = "stage"
    private const val ENV_PROD = "prod"
    private const val ENV_CUSTOM = "custom"

    const val DEFAULT_API_KEY = "_YOUR_API_KEY_"
    const val DEFAULT_MERCHANT_CODE = "ae"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (::prefs.isInitialized) return
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var environment: TabbyEnvironment
        get() = when (prefs.getString(KEY_ENVIRONMENT, ENV_STAGE)) {
            ENV_PROD -> TabbyEnvironment.Prod
            ENV_CUSTOM -> TabbyEnvironment.Custom(
                baseUrl = prefs.getString(KEY_CUSTOM_BASE_URL, "").orEmpty(),
                analyticsUrl = prefs.getString(KEY_CUSTOM_ANALYTICS_URL, "").orEmpty(),
            )

            else -> TabbyEnvironment.Stage
        }
        // commit (synchronous) — restartApp() kills the process right after this via
        // Runtime.exit(), which bypasses Android's normal apply()-flush-on-exit machinery and
        // can lose an async write. Must be on disk before the process dies.
        set(value) = prefs.edit(commit = true) {
            when (value) {
                is TabbyEnvironment.Stage -> putString(KEY_ENVIRONMENT, ENV_STAGE)
                is TabbyEnvironment.Prod -> putString(KEY_ENVIRONMENT, ENV_PROD)
                is TabbyEnvironment.Custom -> {
                    putString(KEY_ENVIRONMENT, ENV_CUSTOM)
                    putString(KEY_CUSTOM_BASE_URL, value.baseUrl)
                    putString(KEY_CUSTOM_ANALYTICS_URL, value.analyticsUrl)
                }
            }
        }

    var apiKey: String
        get() = prefs.getString(KEY_API_KEY, DEFAULT_API_KEY) ?: DEFAULT_API_KEY
        // commit (synchronous) — see [environment] setter comment.
        set(value) = prefs.edit(commit = true) { putString(KEY_API_KEY, value) }

    var merchantCode: String
        get() = prefs.getString(KEY_MERCHANT_CODE, DEFAULT_MERCHANT_CODE) ?: DEFAULT_MERCHANT_CODE
        set(value) = prefs.edit { putString(KEY_MERCHANT_CODE, value) }

    var lang: Lang
        get() = if (prefs.getString(KEY_LANG, Lang.EN.name) == Lang.AR.name) Lang.AR else Lang.EN
        set(value) = prefs.edit { putString(KEY_LANG, value.name) }
}

/** Short label for the QA top bar / badges. */
fun TabbyEnvironment.qaLabel(): String = when (this) {
    is TabbyEnvironment.Stage -> "Stage"
    is TabbyEnvironment.Prod -> "Prod"
    is TabbyEnvironment.Custom -> "Custom"
}

/**
 * Relaunches the app in a fresh process. Required after changing [DemoConfig.environment] or
 * [DemoConfig.apiKey] since [ai.tabby.android.factory.TabbyFactory.setup] only runs once per process.
 */
fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        ?: return
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
    Runtime.getRuntime().exit(0)
}
