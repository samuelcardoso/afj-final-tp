package puc.purchase.model

import jakarta.persistence.*


@Entity
@Table(name = "purchase")
data class Purchase(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val productId: String
)