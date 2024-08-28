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
    val image:String,
    val description: String,
    val weight:Double,
    val measure:String,
    val color:String,
    val category: String,
    val brand:String,
){
    constructor(product: Product): this(
        name = product.name,
        price = product.price,
        image=product.image,
        description= product.description,
        weight=product.weight,
        measure=product.measure,
        color=product.color,
        category= product.category,
        brand=product.brand,
    )

    fun toDomain() =  Product(
        id = this.id,
        name = this.name,
        price = this.price,
        image=this.image,
        description= this.description,
        weight=this.weight,
        measure=this.measure,
        color=this.color,
        category= this.category,
        brand=this.brand,
    )
}

