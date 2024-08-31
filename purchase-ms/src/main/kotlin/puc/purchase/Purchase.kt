package puc.purchase

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType

@Entity
data class Purchase(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)