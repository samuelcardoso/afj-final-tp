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
}



