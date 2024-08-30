package puc.domain.products.services

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import puc.domain.products.model.Product
import puc.infrastructure.repositories.ProductRepository
import puc.infrastructure.entities.ProductEntity
import puc.domain.mappers.ProductMapper
import puc.domain.users.model.User
import puc.domain.users.services.UserService
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(val productRepository: ProductRepository, val userService: UserService) : IProductService {
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    override fun findAll(requestParam: GetAllProductsRequestParam?): List<Product> {
        val allProducts = productRepository.findAll()

        val filterProducts = findAllFilters(requestParam,allProducts)

        return filterProducts.map { ProductMapper.entityToDomain(it)  }
    }

    override fun findById(id: String): Product? {
        var result = productRepository.findById(id).getOrNull()

        return if (result != null) ProductMapper.entityToDomain(result) else null;
    }

    override fun save(product: Product, user: User?): Product {
        user ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with the given id not found.");
        product.userId = user.id
        product.username = user.username
        
        logger.info("Saving product with name ${product.name}")

        val result = productRepository.save(ProductEntity(product))

        logger.info("The product was successfully save under the id ${result.id}")

        return ProductMapper.entityToDomain(result)
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

