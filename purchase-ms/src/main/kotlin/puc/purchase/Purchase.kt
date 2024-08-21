package puc.purchase

import jakarta.persistence.*

@Entity
data class Purchase(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)