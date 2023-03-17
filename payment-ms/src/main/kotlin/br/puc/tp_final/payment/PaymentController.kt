package br.puc.tp_final.payment

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/payment-ms/rest/payment")
class PaymentController(
    val paymentService: PaymentService
) {
    @PostMapping("pay")
    fun pay() {
        paymentService.pay()
    }

    @GetMapping("status/{id}")
    fun status(): String {
        return paymentService.status()
    }
}