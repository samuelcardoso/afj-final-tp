package puc.purchase

import org.springframework.web.bind.annotation.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import puc.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper

@RestController
@RequestMapping("/purchase")
class PurchaseController(val rabbitTemplate: RabbitTemplate, val jwtUtil: JwtUtil) {

    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey}")
    lateinit var routingKey: String

    @PostMapping("/buy")
    fun buy(@RequestHeader("Authorization") token: String, @RequestBody purchaseRequest: List<PurchaseRequest>): ResponseEntity<String> {
        val userId = jwtUtil.extractUserId(token.removePrefix("Bearer "))
        for (purchase in purchaseRequest) {
            val purchaseMessage = PurchaseMessage(userId, purchase.productId, purchase.quantity)
            val objectMapper = ObjectMapper()
            val messageAsString = objectMapper.writeValueAsString(purchaseMessage)
            rabbitTemplate.convertAndSend(exchange, routingKey, messageAsString)
        }
        return ResponseEntity.ok("Purchase request sent.")
    }
}

data class PurchaseRequest(val productId: String, val quantity: Int)