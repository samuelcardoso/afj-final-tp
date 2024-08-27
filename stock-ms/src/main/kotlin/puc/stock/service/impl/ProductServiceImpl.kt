package puc.stock.service.impl

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Service
import puc.stock.client.ProductClient
import puc.stock.exception.ProductNotFoundException
import puc.stock.resources.ProductResource
import puc.stock.service.ProductService
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class ProductServiceImpl(private val client: ProductClient): ProductService {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ProductService::class.java)
    }

    @CircuitBreaker(name = "productClient", fallbackMethod = "getProductByIdFallback")
    override fun findProductById(id: String): ProductResource {
        return this.client.getProductById(id).orElseThrow{ProductNotFoundException("Product not found with id: $id")};
    }

    private fun getProductByIdFallback(id: String, ex: Throwable): Optional<ProductResource> {
        log.info("Exception occurred during attempt to find product with id: $id, with exception message: ${ex.message}")
        return Optional.empty();
    }

}