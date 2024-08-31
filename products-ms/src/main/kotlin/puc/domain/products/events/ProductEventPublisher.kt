package puc.domain.products.events

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ProductEventPublisher(private val rabbitTemplate: RabbitTemplate) {
    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    fun publishProductRegisteredEvent(productRegisteredEvent: ProductRegisteredEvent) {
        val objectMapper = ObjectMapper()
        val messageAsString = objectMapper.writeValueAsString(productRegisteredEvent)

        rabbitTemplate.convertAndSend(exchange, "product.registered", messageAsString)
        println("Published product registered event: $messageAsString")
    }

    fun publishProductDeletedEvent(productDeletedEvent: ProductDeletedEvent) {
        val objectMapper = ObjectMapper()
        val messageAsString = objectMapper.writeValueAsString(productDeletedEvent)

        rabbitTemplate.convertAndSend(exchange, "product.deleted", messageAsString)
        println("Published product deleted event: $messageAsString")
    }
}