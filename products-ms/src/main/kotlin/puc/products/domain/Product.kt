package puc.products.domain

data class Product(
    val id: String? = null,
    val name: String,
    val price: Double,
){
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(!price.isNaN()) { "Price must be a nan" }
        require(price > 0) { "Price must be greater than zero" }
    }
}