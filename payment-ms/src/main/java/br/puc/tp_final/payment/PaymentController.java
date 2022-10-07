package br.puc.tp_final.payment;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("payment")
@Produces({"application/json"})
public class PaymentController {

    @EJB
    private PaymentService paymentService;

    @POST
    @Consumes("application/json")
    public void pay() {
        paymentService.pay();
    }
}