package puc.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import puc.model.PurchaseRequest
import puc.purchase.PurchaseMessage

@Service
class PurchaseService(val rabbitTemplate: RabbitTemplate) {

    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey}")
    lateinit var routingKey: String

    fun sendMessage(purchaseRequest: PurchaseRequest, userId: Long) {
        val purchaseMessage = PurchaseMessage(userId, purchaseRequest.productId, purchaseRequest.quantity)
        val objectMapper = ObjectMapper()
        val messageAsString = objectMapper.writeValueAsString(purchaseMessage)
        rabbitTemplate.convertAndSend(exchange, routingKey, messageAsString)
    }

}