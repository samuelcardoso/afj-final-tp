package puc.stock.model

import jakarta.persistence.*

@Entity
data class Stock(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val productId: String,
    var quantity: Int
)