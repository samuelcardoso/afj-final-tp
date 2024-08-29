package puc.application.dtos

import puc.domain.enums.Category
import puc.domain.products.model.Product
import java.time.LocalDateTime

data class ProductDTO(
    val name: String,
    val price: Double,
    val image:String,
    val description: String,
    val weight:Double,
    val measure:String,
    val color:String,
    val category: Category,
    val brand:String
) {
    fun toDomain(): Product {
        return Product(
            name = this.name,
            price = this.price,
            image =this.image,
            description = this.description,
            weight = this.weight,
            measure =this.measure,
            color =this.color,
            category = category,
            brand = this.brand
        )
    }
}



