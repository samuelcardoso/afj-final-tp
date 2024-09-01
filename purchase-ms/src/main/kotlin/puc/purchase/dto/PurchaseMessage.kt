package puc.purchase.dto

data class PurchaseMessage(
    val id: Long? = 0,
    val productId: String = "",
    val quantity: Int = 0
)