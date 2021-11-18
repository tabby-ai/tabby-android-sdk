# Tabby Android SDK

Tabby SDK for Android makes it easier to integrate Tabby payment platform to your app.

## Initialization

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

## Create Tabby Session

### Prepare Payment Payload

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

### Create Session

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

## Start Tabby Checkout

To start Tabby Checkout you need to create intent and launch Tabby Checkout.

```kotlin
class CheckoutActivity : ComponentActivity() {

    private fun onProductSelected(selectedProduct: Product) {
        val intent = TabbyFactory.tabby.createCheckoutIntent(product = selectedProduct)
        checkoutContract.launch(intent) // contract will be discussed below
    }
}
```

## Receive Checkout Result

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
