package puc.products.domain

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import puc.products.out.database.ProductEntity
import puc.products.out.database.ProductRepository
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(val productRepository: ProductRepository) {
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    fun findAll(): List<Product> = productRepository.findAll().map { it.toDomain() }

    fun findById(id: String): Product? = productRepository.findById(id).getOrNull()?.toDomain()

    fun save(product: Product): Product {
        logger.info("Saving product with name ${product.name}")

        val result = productRepository.save(ProductEntity(product)).toDomain()

        logger.info("The product was successfully save under the id ${result.id}")

        return result
    }
}