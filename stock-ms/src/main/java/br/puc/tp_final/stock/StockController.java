package br.puc.tp_final.stock;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;

@Path("stock")
@Produces({"application/json"})
public class StockController {

    @EJB
    private StockService stockService;

    @POST
    @Path("write_off")
    @Consumes("application/json")
    public void pay() {
        stockService.write_off();
    }

    @GET
    @Path("/status/{id}")
    @Consumes("application/json")
    public String status() {
        return stockService.status();
    }
}