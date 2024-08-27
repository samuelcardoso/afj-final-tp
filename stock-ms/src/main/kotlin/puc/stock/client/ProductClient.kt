package puc.stock.client

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import puc.stock.resources.ProductResource
import java.util.*

@HttpExchange()
interface ProductClient {

    @GetExchange("/{id}")
    fun getProductById(@PathVariable id: String): Optional<ProductResource>
}