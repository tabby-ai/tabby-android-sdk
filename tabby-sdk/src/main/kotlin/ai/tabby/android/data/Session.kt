package ai.tabby.android.data

data class Session(

    val id: String,

    val availableProducts: List<Product>
)

data class Product(

    val type: ProductType,

    val webUrl: String
)
