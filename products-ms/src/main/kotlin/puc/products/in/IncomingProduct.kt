package puc.products.`in`

import puc.products.domain.Product

data class IncomingProduct(
    val name: String,
    val price: Double,
    val image:String,
    val description: String,
    val weight:Double,
    val measure:String,
    val color:String,
    val category: String,
    val brand:String,
) {
    fun toDomain(): Product {
        return Product(
            name = this.name,
            price = this.price,
            image=this.image,
            description= this.description,
            weight = this.weight,
            measure=this.measure,
            color=this.color,
            category= this.category,
            brand=this.brand,
        )
    }
}



