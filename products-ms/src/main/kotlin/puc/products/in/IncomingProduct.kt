package puc.products.`in`

import puc.products.domain.Product

data class IncomingProduct(
    val name: String,
    val price: Double,
) {
    fun toDomain(): Product {
        return Product(
            name = this.name,
            price = this.price
        )
    }

    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(!price.isNaN()) { "Price must be a nan" }
        require(price > 0) { "Price must be greater than zero" }
    }
}



