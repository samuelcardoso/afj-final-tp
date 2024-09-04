package puc.controller

import com.fasterxml.jackson.core.JsonProcessingException
import jakarta.validation.Valid
import org.springframework.amqp.AmqpException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import puc.exceptions.ErrorTryingSoSendMessageMQException
import puc.exceptions.ErrorTryingToProcessJSONException
import puc.gateway.UserMsRestTemplate
import puc.model.PurchaseRequest
import puc.service.PurchaseService
import puc.vo.JWT

@RestController
@RequestMapping("/purchase")
class PurchaseController(
    val purchaseService: PurchaseService,
    val userMsRestTemplate: UserMsRestTemplate
) {

    @PostMapping("/buy")
    fun buy(@RequestHeader("Authorization") token: String, @Valid @RequestBody purchaseRequest: PurchaseRequest): ResponseEntity<String> {
        try {
            val jwt = JWT.create(token, userMsRestTemplate);
            purchaseService.sendMessage(purchaseRequest, jwt.id)
        } catch (e: AmqpException) {
            throw ErrorTryingSoSendMessageMQException("Failed to send message to MQ");
        } catch (e: JsonProcessingException) {
            throw ErrorTryingToProcessJSONException("Failed to process json");
        }

        return ResponseEntity.ok("Purchase request sent.") }
}