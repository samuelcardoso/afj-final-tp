package puc.user

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PurchaseConsumer(val restTemplate: RestTemplate) {

    @RabbitListener(queues = ["\${rabbitmq.queue}"])
    fun receivePurchaseMessage(purchaseMessage: PurchaseMessage) {
        // Call the stock-ms service to decrement stock
        restTemplate.postForObject(
            "http://localhost:8082/write-down",
            StockRequest(purchaseMessage.productId, purchaseMessage.quantity),
            Void::class.java
        )
        // Logic to save purchase in the database can be added here
    }
}

data class StockRequest(val productId: String, val quantity: Int)