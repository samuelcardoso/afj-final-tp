package puc.products.external.database

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import puc.products.domain.Product

@Document(collection = "Product")
data class ProductEntity(
    @Id
    val id: String? = null,
    val name: String,
    val price: Double,
){
    constructor(product: Product): this(
        name = product.name, price = product.price
    )

    fun toDomain() =  Product(
        id = this.id,
        name = this.name,
        price = this.price
    )
}

