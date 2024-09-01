package puc.purchase.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import puc.purchase.dto.PurchaseMessage
import puc.purchase.service.PurchaseService
import puc.purchase.dto.Stock
import puc.purchase.dto.StockRequest
import puc.purchase.excecoes.InsufficientStockException
import puc.purchase.model.Purchase

@Component
class PurchaseConsumer(
        val restTemplate: RestTemplate,
        private val purchaseService: PurchaseService,
        service: PurchaseService
) {
    private val logger = LoggerFactory.getLogger(PurchaseConsumer::class.java)

    @RabbitListener(queues = ["\${rabbitmq.queue}"])
    fun receivePurchaseMessage(purchaseMessage: PurchaseMessage) {

        logger.info("receivePurchaseMessage :")
        try {

            val httpHeaders = HttpHeaders();
            httpHeaders.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")

            // Chama o serviço stock-ms para decrementar o estoque
            val requestBody="";
            val url = UriComponentsBuilder.fromUriString("http://localhost:8082/write-down/${purchaseMessage.productId}/${purchaseMessage.quantity}").toUriString()
            val response = restTemplate.exchange(url, HttpMethod.PUT, HttpEntity<StockRequest>(null, httpHeaders), Stock::class.java)

            // Deserializa a string JSON em um objeto
            val reponseObj = (ObjectMapper()).writeValueAsBytes(response.getBody())
            val stock = (ObjectMapper()).readValue(reponseObj, Stock::class.java)

            val purchase = stock.let {
                Purchase(null, it.productId)
            }

            // Lógica para salvar a compra no banco de dados pode ser adicionada aqui
            purchaseService.save(purchase)
            logger.info("Baixa no estoque realizada para o produto: ${purchaseMessage.productId}")

        } catch (e: Exception) {

            throw InsufficientStockException("Insufficient stock ${e.message}")

        }
    }
}


