package puc.stock.service.impl

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import puc.stock.client.ProductClient
import puc.stock.exception.ProductNotFoundException
import puc.stock.resources.ProductResource
import puc.stock.service.ProductService

@Service
class ProductServiceImpl(private val client: ProductClient): ProductService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @CircuitBreaker(name = "productClient", fallbackMethod = "getProductByIdFallback")
    override fun findProductById(id: String): ProductResource {
        logger.info("=== Buscando produto [{}]", id)
        return this.client.getProductById(id)
            .orElseThrow{ ProductNotFoundException(String.format("Produto com id [%s] não encontrado!", id)) }
    }

    private fun getProductByIdFallback(id: String, ex: Throwable): ProductResource {
        logger.info("Exception occurred during attempt to find product with id: $id, with exception message: ${ex.message}")
        throw ex
    }

}