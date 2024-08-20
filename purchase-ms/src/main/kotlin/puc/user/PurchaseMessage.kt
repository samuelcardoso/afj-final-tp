package puc.user

data class PurchaseMessage(val userId: Long, val productId: String, val quantity: Int)