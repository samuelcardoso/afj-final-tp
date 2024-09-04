package puc.application.consumers

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import puc.domain.products.events.ProductDeletedEvent
import puc.domain.products.events.ProductRegisteredEvent

@Component
class ProductEventConsumer {

    private val objectMapper = ObjectMapper()
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    @RabbitListener(queues = ["product.registered.queue"])
    fun handleProductRegisteredEvent(@Payload message: String) {
        try {
            logger.info("Raw message product registered: $message")

            val productRegisteredEvent = objectMapper.readValue(message, ProductRegisteredEvent::class.java)

            logger.info("Received product registered event: $productRegisteredEvent")
            // Lógica adicional para processar o evento de registro
        } catch (e: Exception) {
            logger.error("Error processing product registered event: ${e.message}")
            e.printStackTrace()
            throw AmqpRejectAndDontRequeueException(e.message, e)
        }
    }

    @RabbitListener(queues = ["product.deleted.queue"])
    fun handleProductDeletedEvent(@Payload message: String) {
        try {
            logger.info("Raw message product deleted: $message")

            val productDeletedEvent = objectMapper.readValue(message, ProductDeletedEvent::class.java)

            logger.info("Received product deleted event: $productDeletedEvent")
            // Lógica adicional para processar o evento de deleção
        } catch (e: Exception) {
            logger.error("Error processing product deleted event: ${e.message}")
            e.printStackTrace()
            throw AmqpRejectAndDontRequeueException(e.message, e)
        }
    }
}
