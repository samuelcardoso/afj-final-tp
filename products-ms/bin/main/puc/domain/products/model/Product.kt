package puc.domain.products.model

import puc.domain.enums.Category
import java.time.LocalDateTime

data class Product(
    val id: String? = null,
    val name: String,
    val price: Double,
    val image:String,
    val description: String,
    val weight:Double,
    val measure:String,
    val color:String,
    val category: Category,
    val brand:String,
    val dataRegister: LocalDateTime? = null
){
    init {

    }
}