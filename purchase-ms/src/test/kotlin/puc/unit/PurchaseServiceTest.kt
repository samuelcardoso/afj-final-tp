package puc.unit

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.test.util.ReflectionTestUtils
import puc.model.PurchaseRequest
import puc.purchase.PurchaseMessage
import puc.service.PurchaseService

@ExtendWith(MockitoExtension::class)
class PurchaseServiceTest {

    @Mock
    lateinit var rabbitTemplate: RabbitTemplate

    @InjectMocks
    lateinit var purchaseService: PurchaseService

    @BeforeEach
    fun setUp() {
        ReflectionTestUtils.setField(purchaseService, "exchange", "purchaseExchange")
        ReflectionTestUtils.setField(purchaseService, "routingKey", "purchaseRoutingKey")
    }

    @Test
    @Throws(Exception::class)
    fun testSendMessage() {
        val userId = 1L
        val purchaseRequest = PurchaseRequest("100", 2)
        val purchaseMessage = PurchaseMessage(userId, purchaseRequest.productId, purchaseRequest.quantity)
        val objectMapper = ObjectMapper()
        val messageAsString = objectMapper.writeValueAsString(purchaseMessage)

        purchaseService.sendMessage(purchaseRequest, userId)

        verify(rabbitTemplate).convertAndSend("purchaseExchange", "purchaseRoutingKey", messageAsString)
    }
}