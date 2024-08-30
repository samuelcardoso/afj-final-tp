package puc.application.dtos

import puc.domain.enums.Category

data class ProductDTO(
    val name: String,
    val price: Double,
    val image:String,
    val description: String,
    val weight:Double,
    val measure:String,
    val color:String,
    val category: Category,
    val brand:String,
    val userId: Long
)



