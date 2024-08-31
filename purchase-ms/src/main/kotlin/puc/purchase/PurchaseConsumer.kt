package puc.purchase

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PurchaseConsumer(
    val restTemplate: RestTemplate,
    private val purchaseService: PurchaseService
) {

    @RabbitListener(queues = ["\${rabbitmq.queue}"])
    fun receivePurchaseMessage(purchaseMessageStr: String) {
        println("Mensagem recebida: $purchaseMessageStr")

        try {
            // Deserializa a string JSON em um objeto PurchaseMessage
            val objectMapper = ObjectMapper()
            val purchaseMessage: PurchaseMessage = objectMapper.readValue(purchaseMessageStr, PurchaseMessage::class.java)

            // Chama o serviço stock-ms para decrementar o estoque
           /* restTemplate.postForObject(
                "http://localhost:8082/write-down",
                StockRequest(purchaseMessage.productId, purchaseMessage.quantity),
                Void::class.java
            )*/

            purchaseService.save(Purchase(productId = purchaseMessage.productId, quantity = purchaseMessage.quantity))
            println("Baixa no estoque realizada para o produto: ${purchaseMessage.productId}")

        } catch (e: Exception) {
            // Captura exceções na desserialização ou na chamada ao serviço
            println("Erro ao processar a mensagem: ${e.message}")
            e.printStackTrace()
        }
    }
}

data class StockRequest(val productId: String, val quantity: Int)
