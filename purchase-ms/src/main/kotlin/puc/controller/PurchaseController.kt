package puc.controller

import org.springframework.amqp.AmqpException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestBody
import com.fasterxml.jackson.core.JsonProcessingException
import puc.gateway.UserMsRestTemplate
import puc.model.PurchaseRequest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import puc.service.PurchaseService
import jakarta.validation.Valid
import puc.exceptions.ErrorTryingSoSendMessageMQException
import puc.exceptions.ErrorTryingToProcessJSONException
import kotlinx.serialization.encodeToString
import puc.vo.JWT

@Serializable
data class ErrorGateway(val timestamp: String, val status: Int, val error: String, val path: String)

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