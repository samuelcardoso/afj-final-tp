package puc.application._shared

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import puc.domain.products.events.ProductDeletedEvent
import puc.domain.products.events.ProductRegisteredEvent
import puc.domain.products.model.Product

@Component
class ProductEventPublisher(private val rabbitTemplate: RabbitTemplate) {
    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    val logger = LoggerFactory.getLogger(this.javaClass)!!

    fun publishProductRegisteredEvent(product: Product) {
        val productRegisterEvent = ProductRegisteredEvent(
            productId = product.id.toString(),
            name = product.name,
            price = product.price
        )

        val objectMapper = ObjectMapper()
        val messageAsString = objectMapper.writeValueAsString(productRegisterEvent)

        rabbitTemplate.convertAndSend(exchange, "product.registered", messageAsString)
        logger.info("Published product registered event: $messageAsString")
    }

    fun publishProductDeletedEvent(idProduct : String) {
        val productDeletedEvent = ProductDeletedEvent(
            productId = idProduct
        )
        val objectMapper = ObjectMapper()
        val messageAsString = objectMapper.writeValueAsString(productDeletedEvent)

        rabbitTemplate.convertAndSend(exchange, "product.deleted", messageAsString)
        logger.info("Published product deleted event: $messageAsString")
    }
}