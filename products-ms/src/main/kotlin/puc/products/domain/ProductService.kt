package puc.products.domain

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import puc.products.external.database.ProductEntity
import puc.products.external.database.ProductRepository
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(val productRepository: ProductRepository) : IProductService{
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    override fun findAll(requestParam: GetAllProductsRequestParam?): List<Product> {
        val allProducts = productRepository.findAll()

        val filterProducts = findAllFilters(requestParam,allProducts)

        return filterProducts.map { it.toDomain() }
    }

    override fun findById(id: String): Product? = productRepository.findById(id).getOrNull()?.toDomain()

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

    private fun findAllFilters(requestParam: GetAllProductsRequestParam?, allProducts: MutableList<ProductEntity>) : List<ProductEntity> =
        allProducts.asSequence().filter { product ->
            requestParam?.name?.let { product.name.contains(it, ignoreCase = true) } ?: true &&
                    requestParam?.price?.let { product.price == it } ?: true &&
                    requestParam?.description?.let { product.description.contains(it, ignoreCase = true) } ?: true &&
                    requestParam?.weight?.let { product.weight == it } ?: true &&
                    requestParam?.measure?.let { product.measure.contains(it, ignoreCase = true) } ?: true &&
                    requestParam?.color?.let { product.color.contains(it, ignoreCase = true) } ?: true &&
                    requestParam?.category?.let { product.category.contains(it, ignoreCase = true) } ?: true &&
                    requestParam?.brand?.let { product.brand.contains(it, ignoreCase = true) } ?: true
        }.toList()

}

