package puc.purchase.dto

data class Stock(
    val id: Long? = 0,
    val productId: String = "",
    var quantity: Int = 0

)