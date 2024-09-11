package puc.stock.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import puc.stock.controller.response.StockResponse
import java.time.LocalDateTime
import java.time.ZoneId

@Entity
data class Stock(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val productId: String,
    var quantity: Int,
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null
) {

    @PrePersist
    fun onPrePersist() {
        val now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onPreUpdate() {
        updatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
    }

    fun toResponse() : StockResponse {
        return StockResponse(this.id!!, this.productId, this.quantity)
    }
}