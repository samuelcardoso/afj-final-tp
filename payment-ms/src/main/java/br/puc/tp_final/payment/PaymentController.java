package br.puc.tp_final.payment;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;

@Path("payment")
@Produces({"application/json"})
public class PaymentController {

    @EJB
    private PaymentService paymentService;

    @POST
    @Path("pay")
    @Consumes("application/json")
    public void pay() {
        paymentService.pay();
    }

    @GET
    @Path("/status/{id}")
    @Consumes("application/json")
    public String status() {
        return paymentService.status();
    }
}