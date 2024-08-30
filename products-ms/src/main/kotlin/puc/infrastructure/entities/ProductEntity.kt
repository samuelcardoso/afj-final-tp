package puc.infrastructure.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import puc.domain.enums.Category
import puc.domain.products.model.Product
import java.time.LocalDateTime

@Document(collection = "Product")
data class ProductEntity(
    @Id
    val id: String? = null,
    var name: String,
    var price: Double,
    var image:String,
    var description: String,
    var weight:Double,
    var measure:String,
    var color:String,
    var category: String,
    var brand:String,
    val dataRegister: LocalDateTime = LocalDateTime.now()
){
    constructor(product: Product): this(
        name = product.name,
        price = product.price,
        image=product.image,
        description= product.description,
        weight=product.weight,
        measure=product.measure,
        color=product.color,
        category= product.category.description,
        brand=product.brand
    )

    fun update(product: Product)
    {
        name = product.name
        price = product.price
        image=product.image
        description= product.description
        weight=product.weight
        measure=product.measure
        color=product.color
        category= product.category.description
        brand=product.brand
    }
}

