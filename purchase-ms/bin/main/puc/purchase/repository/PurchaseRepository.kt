package puc.purchase.repository

import org.springframework.data.jpa.repository.JpaRepository
import puc.purchase.dto.PurchaseMessage
import puc.purchase.model.Purchase

interface PurchaseAppRepository : JpaRepository<Purchase, Long> {
    fun save(purchase: Purchase): Purchase?
}

