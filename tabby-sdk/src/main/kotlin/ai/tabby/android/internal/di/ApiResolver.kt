package ai.tabby.android.internal.di

import kotlin.reflect.KClass

internal object ApiResolver {
    private val apis: MutableMap<KClass<out Api>, Api> = mutableMapOf()

    inline fun <reified TApi : Api> register(api: TApi) {
        apis[TApi::class] = api
    }

    inline fun <reified TApi : Api> get(): TApi = apis.getOrElse(TApi::class) {
        throw Exception("Api was not registered.")
    } as TApi
}

internal inline fun <reified TComponent : Api> apiCreator(creator: () -> TComponent): TComponent {
    val component = creator()
    ApiResolver.register<TComponent>(component)
    return component
}

internal inline fun <reified T : Api> api(): Lazy<T> = lazy { ApiResolver.get() }

internal interface Api

