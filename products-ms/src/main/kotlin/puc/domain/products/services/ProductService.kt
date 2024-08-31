package puc.domain.products.services

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import puc.application.dtos.FilterProductParamsDTO
import puc.application.dtos.PaginatedResponseDTO
import puc.application.dtos.PaginationMetaDTO
import puc.domain.products.model.Product
import puc.infrastructure.repositories.ProductRepository
import puc.infrastructure.entities.ProductEntity
import puc.domain.mappers.ProductMapper
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(val productRepository: ProductRepository) : IProductService{
    val logger = LoggerFactory.getLogger(this.javaClass)!!


    override fun findAll(filterParams: FilterProductParamsDTO): PaginatedResponseDTO<Product> {
        val totalPages = getTotalPages(filterParams.pageSize)

        filterParams.validate(totalPages)

        val pageable = PageRequest.of(filterParams.page.minus(1), filterParams.pageSize)
        val pagedProducts = productRepository.findAll(pageable)

        val filteredProducts = findAllFilters(filterParams, pagedProducts.content)


        return PaginatedResponseDTO(
            data = filteredProducts.map { ProductMapper.entityToDomain(it)  },
            meta = PaginationMetaDTO(
                total = pagedProducts.totalElements,
                perPage = filterParams.pageSize,
                currentPage = filterParams.page,
                lastPage = totalPages
            )
        )
    }

    @Cacheable(value = ["products"], key = "#id")
    override fun findById(id: String): Product? {
        logger.info("Getting product by id ${id}")

        val result = productRepository.findById(id).getOrNull()

        return if (result != null) ProductMapper.entityToDomain(result) else null;
    }

    override fun save(product: Product): Product {
        logger.info("Saving product with name ${product.name}")

        val result = productRepository.save(ProductEntity(product))

        logger.info("The product was successfully save under the id ${result.id}")

        return ProductMapper.entityToDomain(result)
    }

    override fun update(product: Product, id: String): Product? {
        val productDb = productRepository.findById(id).orElseThrow {
            IllegalArgumentException("Product id ${id} not found")
        }

        logger.info("Updating product with id ${id}")

        ProductMapper.domainToEntity(product, productDb)

        productRepository.save(productDb)

        logger.info("The product ${productDb.id} was successfully updated")

        return ProductMapper.entityToDomain(productDb)
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



    private fun findAllFilters(requestParam: FilterProductParamsDTO?, allProducts: MutableList<ProductEntity>) : List<ProductEntity> =
        allProducts.asSequence().filter { product ->
            requestParam?.name?.let { product.name.contains(it, ignoreCase = true) } ?: true &&
                    requestParam?.category?.let { product.category.contains(it, ignoreCase = true) } ?: true
        }.toList()

}

