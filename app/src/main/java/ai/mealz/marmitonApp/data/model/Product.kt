package ai.mealz.marmitonApp.data.model


data class Product(
    val id: String,
    val attributes: Attributes,
    var quantity: Int = 0
) {
    val priceWithQuantity: Double
        get() {
            if (quantity == 0) {
                return attributes.price
            }
            return attributes.price * quantity
        }

    val identifier: String
        get() = "id_$id"

    override fun toString(): String {
        return "Name -> ${attributes.name} | Price -> $priceWithQuantity | Quantity -> $quantity"
    }

    companion object {
        val empty = Product("", Attributes("", "", 0.0, "", ""))
    }
}

data class Attributes(
    val name: String,
    val image: String,

    val price: Double,
    val extId: String,
    val description: String? = ""
)

