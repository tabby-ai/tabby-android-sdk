# Tabby Android SDK

Tabby SDK for Android makes it easier to integrate you app with Tabby payment platform.

## Requirements

Android 5.0 (API level 21) and above.

## Integration

Add Tabby Android SDK dependency to your app's `build.gradle`:

```groovy
dependencies {
    implementation 'ai.tabby:tabby-android:1.0.1'
}
```

## Getting Started

Before using functions of Tabby SDK, it has to be initialized. The ways of initialization differ depending on whether you app is using Dagger or not. If you don't use Dagger, please read [Initialization via Factory](#initialization-via-factory) chapter. If you DO use Dagger refer to [Initialization of Dagger Components](#initialization-of-dagger-components).

---

**NOTE**

You should use only one of these initialization approaches in the single app.

---

### Initialization via Factory

Tabby SDK requires one-time initialization which can be done on app's start:

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TabbyFactory.setup(this, "__API_KEY_HERE__")
    }
}
```

Once initialized, `Tabby` instance always available via `TabbyFactory.tabby` property.

See [source code](demoapp/src/main/java/ai/tabby/demoapp)

### Initialization of Dagger Components

Declare you own component to be able to inject Tabby instance to your code.

```kotlin
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Component(
    dependencies = [
        TabbyComponent::class // your component must depend on TabbyComponent
    ]
)
@AppScope
interface MyTabbyComponent {

    fun provideTabby(): Tabby

    // Add methods to inject Tabby instance to your code
    fun inject(activity: CheckoutActivity)
}
```

Implement `TabbyComponentDependencies` to provide `context` and your `API_KEY` to the `TabbyComponent`.

```kotlin
class TabbyComponentDependenciesImpl(
    private val context: Context,
    private val apiKey: String
) : TabbyComponentDependencies {
    override fun provideContext(): Context = context
    override fun provideApiKey(): String = apiKey
}
```

Create your component and keep its reference in the `Application` instance.

```kotlin
class App : Application() {

    val myTabbyComponent: MyTabbyComponent

    init {
        // Tabby setup
        val tabbyDependencies = TabbyComponentDependenciesImpl(
            context = this,             // application context
            apiKey = "__API_KEY_HERE__" // you api key
        )

        // Create tabby component and link it to the injectable component
        val tabbyComponent = TabbyComponent.create(dependencies = tabbyDependencies)
        myTabbyComponent = DaggerMyTabbyComponent.builder()
            .tabbyComponent(tabbyComponent)
            .build()
    }
}
```

Once `MyTabbyComponent` is created you can inject Tabby instance to the other parts of your app's code.

See [source code](demoapp-di/src/main/java/ai/tabby/demoappdi)

### Creating Tabby Session

---

**NOTE**

To reduce the size of code examples, further snippets assume that Tabby instance was initialized using [Factory](#initialization-via-factory) approach. You'll see that Tabby instance is accessed via `TabbyFactory.tabby`. If you use Dagger, you can inject Tabby instance to your code using `MyTabbyComponent` described above.

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
                unitPrice = BigDecimal(300),
                quantity = 1
            )
        ),
        shippingAmount = BigDecimal(50),
        taxAmount = BigDecimal(100)
    ),
    shippingAddress = ShippingAddress(
        address = "Sample Address #2",
        city = "Dubai"
    )
)
```

#### Creating Session

Usually Tabby session is created when your checkout activity is created.

```kotlin
class CheckoutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create tabby session once activity is created
        lifecycleScope.launchWhenCreated {
            val session = TabbyFactory.tabby.createSession(
                merchantCode = "ae",
                lang = Lang.EN,
                payment = tabbyPayment
            )

            // Update UI with Tabby products
            withContext(Dispatchers.Main) {
                for (product in session.availableProducts) {
                    // ...
                }
            }
        }
    }
}
```

`TabbySession` contains a list of available Tabby Products, which can be accessed via `tabbySession.availableProducts`. Your app should display these products on checkout activity. When user selects one of the products, you app starts Tabby Checkout.

### Starting Tabby Checkout

To start Tabby Checkout you need to create intent and launch Tabby Checkout.

```kotlin
class CheckoutActivity : ComponentActivity() {

    private fun onProductSelected(selectedProduct: Product) {
        val intent = TabbyFactory.tabby.createCheckoutIntent(product = selectedProduct)
        checkoutContract.launch(intent) // contract will be discussed below
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
                                                  //    TabbyFactory.tabby.createSession(...) again
        }
    }
}
```

## UI Components

**TODO**

## License

```
Copyright 2022 Tabby LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
