package puc.application.consumers

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import puc.domain.products.events.ProductDeletedEvent
import puc.domain.products.events.ProductRegisteredEvent

@Component
class ProductEventConsumer {

    private val objectMapper = ObjectMapper()

    @RabbitListener(queues = ["product.registered.queue"])
    fun handleProductRegisteredEvent(@Payload message: String) {
        try {
            println("Raw message: $message")
            val productRegisteredEvent = objectMapper.readValue(message, ProductRegisteredEvent::class.java)
            println("Received product registered event: $productRegisteredEvent")
            // Lógica adicional para processar o evento de registro
        } catch (e: Exception) {
            println("Error processing product registered event: ${e.message}")
            e.printStackTrace()
            throw AmqpRejectAndDontRequeueException(e.message, e)
        }
    }

    @RabbitListener(queues = ["product.deleted.queue"])
    fun handleProductDeletedEvent(@Payload message: String) {
        try {
            println("Raw message: $message")
            val productDeletedEvent = objectMapper.readValue(message, ProductDeletedEvent::class.java)
            println("Received product deleted event: $productDeletedEvent")
            // Lógica adicional para processar o evento de deleção
        } catch (e: Exception) {
            println("Error processing product deleted event: ${e.message}")
            e.printStackTrace()
            throw AmqpRejectAndDontRequeueException(e.message, e)
        }
    }
}
