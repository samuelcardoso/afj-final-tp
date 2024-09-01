package puc.purchase

import com.fasterxml.jackson.core.JsonProcessingException
import org.springframework.amqp.AmqpException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import puc.gateway.UserMsRestTemplate
import puc.model.PurchaseRequest
import puc.service.PurchaseService
import puc.util.JwtUtil
import puc.vo.JWT

@RestController
@RequestMapping("/purchase")
class PurchaseController(
    val purchaseService: PurchaseService,
    val userMsRestTemplate: UserMsRestTemplate
) {

    @PostMapping("/buy")
    fun buy(@RequestHeader("Authorization") token: String, @RequestBody purchaseRequest: PurchaseRequest): ResponseEntity<String> {
        try {
            val jwt = JWT.create(token, userMsRestTemplate);
            purchaseService.sendMessage(purchaseRequest, jwt.id)
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