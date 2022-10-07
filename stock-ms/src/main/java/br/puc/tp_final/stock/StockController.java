package br.puc.tp_final.stock;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;

@Path("stock")
@Produces({"application/json"})
public class StockController {

    @EJB
    private StockService stockService;

    @GET
    @Consumes("application/json")
    public String pay() {
        return stockService.status();
    }
}