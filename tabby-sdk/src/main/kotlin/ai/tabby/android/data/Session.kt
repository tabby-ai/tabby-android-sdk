package ai.tabby.android.data

enum class ProductType {
    PAY_LATER,
    INSTALLMENTS,
}

data class Session(

    val id: String,

    val availableProducts: List<Product>
)

data class Product(

    val type: ProductType,

    val webUrl: String
)
