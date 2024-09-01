package puc.purchase.controller

import org.springframework.web.bind.annotation.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import puc.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import puc.purchase.consumer.PurchaseConsumer
import puc.purchase.dto.PurchaseMessage
import puc.purchase.dto.PurchaseRequest

@RestController
@RequestMapping("/purchase")
class PurchaseController(val rabbitTemplate: RabbitTemplate, val jwtUtil: JwtUtil, val purchaseConsumer: PurchaseConsumer) {

    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey}")
    lateinit var routingKey: String

    @PostMapping("/buy")
    fun buy(@RequestHeader("Authorization") token: String, @RequestBody purchaseRequest: PurchaseRequest): ResponseEntity<String> {
        val userId = jwtUtil.extractUserId(token.removePrefix("Bearer "))
        val purchaseMessage = PurchaseMessage(userId, purchaseRequest.productId, purchaseRequest.quantity)
        val objectMapper = ObjectMapper()
        val messageAsString = objectMapper.writeValueAsString(purchaseMessage)
        //rabbitTemplate.convertAndSend(exchange, routingKey, messageAsString)
        purchaseConsumer.receivePurchaseMessage(purchaseMessage)
        return ResponseEntity.ok("Purchase request sent.")
    }
}

