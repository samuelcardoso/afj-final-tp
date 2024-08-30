package puc.purchase

import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseAppRepository : JpaRepository<Purchase, Long> {
    fun save(purchase: Purchase): Purchase?
}

