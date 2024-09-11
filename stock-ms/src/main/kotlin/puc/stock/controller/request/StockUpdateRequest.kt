package puc.stock.controller.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class StockUpdateRequest(
    @field:NotNull(message = "O id do produto não pode ser nulo")
    @field:NotEmpty(message = "O id do produto não pode ser vazio")
    val productId: String?,
    @field:NotNull(message = "A quantidade não pode ser nula")
    @field:Positive(message = "Insira uma valor maior ou igual a zero")
    val quantity: Int?
)
