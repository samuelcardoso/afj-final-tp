package puc.purchase.service

import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import puc.purchase.dto.PurchaseMessage
import puc.purchase.model.Purchase
import puc.purchase.repository.PurchaseAppRepository
import puc.util.JwtUtil

@Service
class PurchaseService(val purchaseRepository: PurchaseAppRepository, val jwtUtil: JwtUtil) {

    @Throws(HttpClientErrorException::class)
    fun save(purchase: Purchase): Purchase? {
        try {
            return purchaseRepository.save(purchase)
        } catch (ex: HttpClientErrorException) {
            throw Exception("Couldn't save purchase!")
        }

    }
}