package puc.purchase

data class PurchaseMessage(
    val userId: Long = 0,
    val productId: String = "",
    val quantity: Int = 0
)