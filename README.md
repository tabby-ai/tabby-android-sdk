# Tabby Android SDK

Tabby SDK for Android makes it easier to integrate you app with Tabby payment platform.

## Requirements

Android 6.0 (API level 23) and above.

## Demo App

The [`demoapp`](demoapp) module serves two purposes:

- **QA sandbox** — launch it, open **Settings** to pick Stage/Prod (or a custom environment) and
  set an API key, then use the **payment builder** home screen to pick a test-data preset or edit
  the amount/currency/merchant code/customer & order fields and start a checkout — no rebuild
  needed (changing environment/API key restarts the app to apply, since the SDK can only be set up
  once per process). Every run shows its outcome (success/rejected/cancelled/error) with the raw
  SDK payload on screen, copyable for bug reports. The **Widgets** screen exercises every
  standalone widget (`TabbyInstallmentsWidget`, `TabbySnippetWidget`, `TabbySnippetView`,
  `TabbyCardSnippetView`) against the same payment.
- **Integration reference** — `App.kt`, `CheckoutActivity.kt` and `CheckoutViewModel.kt` (in the
  `ai.tabby.demoapp` root package) show the minimal, real-world shape of an integration and mirror
  the snippets below. Everything under `ai.tabby.demoapp.qa` is testing scaffolding for the sandbox
  above — merchants integrating the SDK should look at the root package, not `qa`.

## Integration

Add Tabby Android SDK dependency to your app's `build.gradle`:

```groovy
dependencies {
    implementation("ai.tabby:tabby-android:3.0.0")
}
```

## Getting Started

Before using functions of Tabby SDK, it has to be initialized once on app start. `setup` is a suspend function — call it from a coroutine scope:

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            TabbyFactory.setup(this@App, "__API_KEY_HERE__", TabbyEnvironment.Prod)
        }
    }
}
```

Once initialized, the `Tabby` instance is available via:

- `TabbyFactory.tabby()` — suspend function, waits for initialization to complete.
- `TabbyFactory.tabbyOrNull()` — non-suspend, returns `null` if not yet initialized.

See [source code](demoapp/src/main/java/ai/tabby/demoapp)

### Creating Tabby Session

---

**NOTE**

Further snippets access `Tabby` via the suspend function `TabbyFactory.tabby()` — call it inside a coroutine scope.

---

#### Preparing Payment Payload

The following code snippet shows example of simple always-successful payment:

```kotlin
val tabbyPayment = TabbyPayment(
    amount = BigDecimal(340),
    currency = Currency.AED,
    description = "tabby Store Order #33",
    buyer = Buyer(
        email = "successful.payment@tabby.ai",  // Always successful
        phone = "500000001",
        name = "Yazan Khalid"
    ),
    order = Order(
        refId = "#xxxx-xxxxxx-xxxx",
        items = listOf(
            OrderItem(
                refId = "SKU123",
                title = "Pink jersey",
                description = "Jersey",
                productUrl = "https://tabby.store/p/SKU123",
                unitPrice = BigDecimal(300.00),
                quantity = 1
            )
        ),
        shippingAmount = BigDecimal(50),
        taxAmount = BigDecimal(100)
    ),
    shippingAddress = ShippingAddress(
        address = "Sample Address #2",
        city = "Dubai",
        zip = "11111"
    ),
    buyerHistory = BuyerHistory(
        // registeredSince is a Date and Date formatter is only one of the options how to get it for particular Date.
        // We suggest to use LocalDateTime. It's not possible from our side because of the minSdk version.
        // Date(LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC))
        registeredSince = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.ENGLISH
        ).parse("2019-08-24T14:15:22Z")!!,
        loyaltyLevel = 0,
        wishlistCount = 1,
        isSocialNetworksConnected = false,
        isPhoneNumberVerified = false,
        isEmailVerified = false,
    ),
    orderHistory = listOf(
        OrderHistory(
            // purchasedAt is a Date and Calendar is only one of the options how to get it for particular Date.
            // We suggest to use LocalDateTime. It's not possible from our side because of the minSdk version.
            // Date(LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC))
            purchasedAt = GregorianCalendar.getInstance().apply {
                set(2019, 8, 24)
            }.time,
            amount = BigDecimal.ONE,
            paymentMethod = PaymentMethod.CARD,
            status = Status.NEW,
            buyer = Buyer(
                email = "test@gmail.com",
                phone = "+995555466567",
                name = "Denis",
                dob = "2019-08-24",
            ),
            shippingAddress = ShippingAddress(
                address = "Tbel-Abuseridze",
                city = "Batumi",
                zip = "6010",
            ),
            items = listOf(
                OrderItem(
                    refId = "1242532",
                    title = "Test item"
                )
            ),
        )
    ),
    meta = emptyMap(),
)
```

#### Creating Session

Tabby session is typically created from a `ViewModel` using `viewModelScope`:

```kotlin
class CheckoutViewModel : ViewModel() {

    fun createSession(tabbyPayment: TabbyPayment, merchantCode: String, lang: Lang) {
        viewModelScope.launch {
            val session = TabbyFactory.tabby().createSession(
                merchantCode = merchantCode,
                lang = lang,
                payment = tabbyPayment
            )
            for (product in session.availableProducts) {
                // ...
            }
        }
    }
}
```

`TabbySession` contains a list of available Tabby Products, which can be accessed via `tabbySession.availableProducts`. Your app should display these products on checkout activity. When user selects one of the products, you app starts Tabby Checkout.

### Starting Tabby Checkout

To start Tabby Checkout you need to create an intent and launch it. Because `tabby()` is a suspend function, create the intent inside a coroutine:

```kotlin
class CheckoutViewModel : ViewModel() {

    suspend fun createCheckoutIntent(product: Product): Intent =
        TabbyFactory.tabby().createCheckoutIntent(product = product)
}

class CheckoutActivity : ComponentActivity() {

    private fun onProductSelected(selectedProduct: Product) {
        lifecycleScope.launch {
            val intent = viewModel.createCheckoutIntent(selectedProduct)
            checkoutContract.launch(intent) // contract will be discussed below
        }
    }
}
```

### Receiving Checkout Result

When your app launches Tabby Checkout, a web view is shown allowing user to confirm their purchase. Once user is authorized (or rejected), result is returned to your checkout activity contract.

```kotlin
class CheckoutActivity : ComponentActivity() {

    private val checkoutContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    result.tabbyResult?.let { tabbyResult ->
                        onCheckoutResult(tabbyResult)
                    } ?: // Tabby result is null
                }
                else -> {
                    // Result is not OK
                }
            }
        }

    private fun onCheckoutResult(tabbyResult: TabbyResult) {
        when (tabbyResult.result) {
            TabbyResult.Result.AUTHORIZED ->  { } // Purchase is authorized
            TabbyResult.Result.REJECTED ->    { } // Purchase is rejected
            TabbyResult.Result.CLOSED ->      { } // Tabby Checkout activity was closed
            TabbyResult.Result.EXPIRED ->     { } // Tabby Session is expired, you need to call
                                                  //    TabbyFactory.tabby().createSession(...) again
        }
    }
}
```

## UI Components

### Card Snippet

`TabbyCardSnippet` is a Compose widget that displays an interactive card-based snippet loaded from the Tabby widgets endpoint. It requires `merchantCode` in addition to the payment object.

**Jetpack Compose:**

```kotlin
import ai.tabby.android.data.Lang
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.ui.dynamic.TabbyCardSnippet

@Composable
fun MyCardSnippet(tabbyPayment: TabbyPayment) {
    TabbyCardSnippet(
        tabbyPayment = tabbyPayment,
        merchantCode = "ae",
        lang = Lang.EN,
        installmentsCount = 4,
        modifier = Modifier.fillMaxWidth(),
    )
}
```

**Traditional View (XML / non-Compose):**

```kotlin
import ai.tabby.android.data.Lang
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.ui.dynamic.TabbyCardSnippetView

@Composable
fun MyCardSnippetView(tabbyPayment: TabbyPayment) {
    AndroidView(
        factory = { context ->
            TabbyCardSnippetView(context)
        },
        update = { widget ->
            widget.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            widget.tabbyPayment = tabbyPayment
            widget.merchantCode = "ae"
            widget.lang = Lang.EN
        }
    )
}
```

### Product Page Snippet

```kotlin
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.ui.TabbySnippetWidget

...

@Composable
fun TabbySnippetWidgetComposable(tabbyPayment: TabbyPayment) {
    AndroidView(
        factory = { context ->
            TabbySnippetWidget(context)
        },
        update = { widget ->
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            widget.layoutParams = params
            widget.amount = tabbyPayment.amount
            widget.currency = tabbyPayment.currency
        }
    )
}
```

### Checkout Snippet

```kotlin
import ai.tabby.android.data.TabbyPayment
import ai.tabby.android.ui.TabbyInstallmentsWidget

...

@Composable
fun TabbyInstallmentsWidgetComposable(tabbyPayment: TabbyPayment) {
    AndroidView(
        factory = { context ->
            TabbyInstallmentsWidget(context)
        },
        update = { widget ->
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            widget.layoutParams = params
            widget.amount = tabbyPayment.amount
            widget.currency = tabbyPayment.currency
        }
    )
}
```

## Migration Guide: 1.x → 2.0.0

### `TabbyFactory.setup()` is now a suspend function

In 1.x, `setup()` was synchronous and could be called directly in `Application.onCreate()`:

```kotlin
// 1.x
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TabbyFactory.setup(this, "__API_KEY_HERE__", TabbyEnvironment.Prod)
    }
}
```

In 2.0.0, `setup()` is a suspend function and must be called from a coroutine:

```kotlin
// 2.0.0
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            TabbyFactory.setup(this@App, "__API_KEY_HERE__", TabbyEnvironment.Prod)
        }
    }
}
```

### `TabbyFactory.tabby` property replaced by `tabby()` suspend function

In 1.x, the `Tabby` instance was accessed via the `TabbyFactory.tabby` property:

```kotlin
// 1.x
val session = TabbyFactory.tabby.createSession(...)
val intent  = TabbyFactory.tabby.createCheckoutIntent(product = selectedProduct)
```

In 2.0.0, `tabby` is a suspend function. All call sites must be inside a coroutine scope:

```kotlin
// 2.0.0
viewModelScope.launch {
    val session = TabbyFactory.tabby().createSession(...)
}

suspend fun createCheckoutIntent(product: Product): Intent =
    TabbyFactory.tabby().createCheckoutIntent(product = product)
```

Use `TabbyFactory.tabbyOrNull()` if you need synchronous, non-blocking access and can handle a `null` result when the SDK is not yet initialized.

### Dagger integration removed

In 1.x, the SDK exposed a Dagger component (`TabbyComponent`) that could be declared as a dependency of your own Dagger component. You implemented `TabbyComponentDependencies` and called `TabbyComponent.create()` to wire everything up:

```kotlin
// 1.x — no longer supported
class TabbyComponentDependenciesImpl(
    private val context: Context,
    private val apiKey: String,
    private val environment: TabbyEnvironment
) : TabbyComponentDependencies {
    override fun getContext(): Context = context
    override fun getApiKey(): String = apiKey
    override fun getEnv(): TabbyEnvironment = environment
}

@Component(dependencies = [TabbyComponent::class])
@AppScope
interface MyTabbyComponent {
    fun provideTabby(): Tabby
    fun inject(activity: CheckoutActivity)
}

class App : Application() {

    val myTabbyComponent: MyTabbyComponent

    init {
        val tabbyComponent = TabbyComponent.create(
            dependencies = TabbyComponentDependenciesImpl(this, "__API_KEY_HERE__", TabbyEnvironment.Prod)
        )
        myTabbyComponent = DaggerMyTabbyComponent.builder()
            .tabbyComponent(tabbyComponent)
            .build()
    }
}
```

In 2.0.0, Dagger has been removed from the SDK. `TabbyComponent` is now a plain interface and `TabbyComponent.create()` no longer exists. To migrate:

1. Remove `TabbyComponent` from your Dagger component dependencies.
2. Delete your `TabbyComponentDependenciesImpl` and `DaggerMyTabbyComponent` setup code.
3. Initialize the SDK with `TabbyFactory.setup()` inside a coroutine on app start (see above).
4. Replace injected `Tabby` references with `TabbyFactory.tabby()` calls inside coroutine scopes.

```kotlin
// 2.0.0 — replace injected Tabby with TabbyFactory.tabby()
class CheckoutViewModel : ViewModel() {
    fun createSession(tabbyPayment: TabbyPayment) {
        viewModelScope.launch {
            val session = TabbyFactory.tabby().createSession(
                merchantCode = "ae",
                lang = Lang.EN,
                payment = tabbyPayment
            )
            // ...
        }
    }
}

## Changelog

### 2.0.0

- **Dagger removed** — the SDK no longer depends on Dagger. The `TabbyComponent` Dagger component and its companion `create()` factory have been removed. `TabbyComponent` is now a plain interface returned by `TabbyFactory.setupTabbyComponent()`. See [Migration Guide](#migration-guide-1x--200) if your app used the Dagger integration.
- **Public API changes**:
  - `TabbyFactory.setup()` is now a suspend function — must be called from a coroutine scope (was synchronous in 1.x).
  - `TabbyFactory.tabby` property replaced by `TabbyFactory.tabby(): Tabby` suspend function — all call sites must be inside a coroutine.
  - `TabbyFactory.tabbyOrNull(): Tabby?` added — synchronous access that returns `null` if the SDK is not yet initialized.
  - `TabbyComponent.apiKey` type changed from `ApiKey` to `String`.
  - `TabbyComponent.environment` property removed.
- **New widget: `TabbyCardSnippet` / `TabbyCardSnippetView`** — a dynamic Compose widget and its View-based wrapper that loads an interactive card snippet from the Tabby widgets endpoint. See [Card Snippet](#card-snippet).

## License

This SDK is released under the [MIT License](LICENSE).
