package puc.purchase

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import puc.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper

@RestController
@RequestMapping("/purchase")
class PurchaseController(
    private val rabbitTemplate: RabbitTemplate,
    private val jwtUtil: JwtUtil
) {

    private val logger = LoggerFactory.getLogger(PurchaseController::class.java)

    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey}")
    lateinit var routingKey: String

    @PostMapping("/buy")
    fun buy(
        @RequestHeader("Authorization") token: String,
        @RequestHeader("CorrelationId", required = false) correlationId: String?,
        @RequestBody purchaseRequest: PurchaseRequest
    ): ResponseEntity<String> {
        val userId = jwtUtil.extractUserId(token.removePrefix("Bearer "))
        val purchaseMessage = PurchaseMessage(userId, purchaseRequest.productId, purchaseRequest.quantity)
        val objectMapper = ObjectMapper()
        val messageAsString = objectMapper.writeValueAsString(purchaseMessage)

        val correlationIdToUse = correlationId ?: generateCorrelationId()
        logger.info("Processing purchase for userId: $userId, productId: ${purchaseRequest.productId}, quantity: ${purchaseRequest.quantity}, correlationId: $correlationIdToUse")

        // Set the correlationId header in the RabbitMQ message
        val messagePostProcessor = MessagePostProcessor { message ->
            message.messageProperties.setHeader("correlationId", correlationIdToUse)
            message
        }

        rabbitTemplate.convertAndSend(exchange, routingKey, messageAsString, messagePostProcessor)
        logger.info("Purchase message sent to RabbitMQ with correlationId: $correlationIdToUse")

        return ResponseEntity.ok("Purchase request sent.")
    }

    private fun generateCorrelationId(): String {
        // Generate a simple correlationId if not provided (e.g., using UUID)
        return java.util.UUID.randomUUID().toString()
    }
}
