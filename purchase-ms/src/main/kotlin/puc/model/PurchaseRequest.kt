package puc.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Min

data class PurchaseRequest(
    @field:NotBlank(message = "ProductId cannot be empty")
    @field:Size(min = 1, message = "ProductId must be at least 1")
    val productId: String,

    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int
)