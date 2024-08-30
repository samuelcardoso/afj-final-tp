package puc.products.domain

import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import puc.products.external.database.ProductEntity
import puc.products.external.database.ProductRepository
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(val productRepository: ProductRepository) : IProductService{
    val logger = LoggerFactory.getLogger(this.javaClass)!!


    override fun findAll(filterParams: FilterProductParams): PaginatedResponse<Product> {
        val totalPages = getTotalPages(filterParams.pageSize)

        filterParams.validate(totalPages)

        val pageable = PageRequest.of(filterParams.page.minus(1), filterParams.pageSize)
        val pagedProducts = productRepository.findAll(pageable)

        val filteredProducts = findAllFilters(filterParams, pagedProducts.content)


        return PaginatedResponse(
            data = filteredProducts.map { it.toDomain() },
            meta = PaginationMeta(
                total = pagedProducts.totalElements,
                perPage = filterParams.pageSize,
                currentPage = filterParams.page,
                lastPage = totalPages
            )
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



    private fun findAllFilters(requestParam: FilterProductParams?, allProducts: MutableList<ProductEntity>) : List<ProductEntity> =
        allProducts.asSequence().filter { product ->
            requestParam?.name?.let { product.name.contains(it, ignoreCase = true) } ?: true &&
                    requestParam?.category?.let { product.category.contains(it, ignoreCase = true) } ?: true
        }.toList()


}

