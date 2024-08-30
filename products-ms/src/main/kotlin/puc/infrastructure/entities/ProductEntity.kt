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
    val name: String,
    val price: Double,
    val image:String,
    val description: String,
    val weight:Double,
    val measure:String,
    val color:String,
    val category: String,
    val brand:String,
    val dataRegister: LocalDateTime = LocalDateTime.now(),
    val userId: Long,
    val username: String
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
        brand=product.brand,
        userId=product.userId,
        username=product.username!!
    )
}

