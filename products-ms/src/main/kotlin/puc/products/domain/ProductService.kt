package puc.products.domain

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import puc.products.out.database.ProductRepository

@Service
class ProductService(val productRepository: ProductRepository) {
    val logger = LoggerFactory.getLogger(this.javaClass)

    fun findAll(): List<Product> = productRepository.findAll()
    fun save(product: Product): Product {
        logger.info("Saving product with name ${product.name}")

        val result = productRepository.save(product)

        logger.info("The product was successfully save under the id ${result.id}")

        return result
    }
}