package puc.products.domain

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import puc.products.external.database.ProductEntity
import puc.products.external.database.ProductRepository
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(val productRepository: ProductRepository) : IProductService{
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    private val defaultPageSize = 30

    override fun findAll(requestParam: GetAllProductsRequestParam?): PaginatedResponse<Product> {
        val page = requestParam?.page?.minus(1) ?: 0
        val pageSize = requestParam?.pageSize ?: defaultPageSize
        val totalPages = getTotalPages(pageSize)

        validatePage(page, totalPages)

        val pageable = PageRequest.of(page, pageSize)
        val pagedProducts = productRepository.findAll(pageable)

        val filteredProducts = findAllFilters(requestParam, pagedProducts.content)

        val meta = PaginationMeta(
            total = pagedProducts.totalElements,
            perPage = pageSize,
            currentPage = page + 1,
            lastPage = totalPages
        )

        return PaginatedResponse(
            data = filteredProducts.map { it.toDomain() },
            meta = meta
        )
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

    private fun getTotalPages(pageSize: Int): Int {
        val totalElements = productRepository.count()
        return ((totalElements / pageSize) + if (totalElements % pageSize > 0) 1 else 0).toInt()
    }

    fun validatePage(page: Int, totalPages: Int) {
        if (page < 0) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page number cannot be less than 1."
            )
        }

        if (page >= totalPages) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Requested page does not exist. Last available page: $totalPages"
            )
        }
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

