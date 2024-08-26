package puc.products.domain

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import puc.products.out.database.ProductEntity
import puc.products.out.database.ProductRepository

@Service
class ProductService(val productRepository: ProductRepository) : IProductService{
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    override fun findAll(): List<Product> = productRepository.findAll().map { it.toDomain() }


    override fun save(product: Product): Product {
        logger.info("Saving product with name ${product.name}")

        val result = productRepository.save(ProductEntity(product)).toDomain()

        logger.info("The product was successfully save under the id ${result.id}")

        return result
    }

    override fun delete(productId: String) {
        productRepository.deleteById(productId)
    }

    override fun update() {
        TODO("Not yet implemented")
    }
}

