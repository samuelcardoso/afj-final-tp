package puc.purchase

import com.fasterxml.jackson.core.JsonProcessingException
import org.springframework.web.bind.annotation.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import puc.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.AmqpException
import puc.model.PurchaseRequest
import puc.service.PurchaseService

@RestController
@RequestMapping("/purchase")
class PurchaseController(val purchaseService: PurchaseService, val jwtUtil: JwtUtil) {

    @PostMapping("/buy")
    fun buy(@RequestHeader("Authorization") token: String, @RequestBody purchaseRequest: PurchaseRequest): ResponseEntity<String> {
        try {
            val userId = jwtUtil.getUserId(token)
            purchaseService.sendMessage(purchaseRequest, userId)
        }catch (e: AmqpException) {
            return ResponseEntity.internalServerError().body("Failed to send message to MQ");
        } catch (e: JsonProcessingException) {
            return ResponseEntity.internalServerError().body("Failed to process json");
        } catch (e: RuntimeException) {
            return ResponseEntity.status(401).body(e.message);
        }

        return ResponseEntity.ok("Purchase request sent.")
    }
}