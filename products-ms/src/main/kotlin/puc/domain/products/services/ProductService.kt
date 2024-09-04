package puc.domain.products.services

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import puc.application.dtos.FilterProductParamsDTO
import puc.application.dtos.PaginatedResponseDTO
import puc.application.dtos.PaginationMetaDTO
import puc.domain.products.model.Product
import puc.infrastructure.repositories.ProductRepository
import puc.infrastructure.entities.ProductEntity
import puc.domain.products.mappers.ProductMapper
import puc.application.controllers.ProductController
import puc.domain.enums.Category
import puc.domain.products.services.exceptions.ProductNotFoundException
import puc.domain.users.model.User
import puc.domain.users.services.UserService

@Service
class ProductService(
    val productRepository: ProductRepository,
    val userService: UserService
) : IProductService{

    val logger = LoggerFactory.getLogger(this.javaClass)!!

    override fun findAll(filterParams: FilterProductParamsDTO): PaginatedResponseDTO<Product> {
        val totalPages = getTotalPages(filterParams.pageSize)

        filterParams.validate(totalPages)

        val pageable = PageRequest.of(filterParams.page.minus(1), filterParams.pageSize)
        val pagedProducts = productRepository.findAll(pageable)
        val filteredProducts = findAllFilters(filterParams, pagedProducts.content)
        val resultProducts = filteredProducts.map {
            ProductMapper.entityToDomain(it)
        }

        linkToProductController(resultProducts)

        return PaginatedResponseDTO(
            data = resultProducts,
            meta = PaginationMetaDTO(
                total = pagedProducts.totalElements,
                perPage = filterParams.pageSize,
                currentPage = filterParams.page,
                lastPage = totalPages
            )
        )
    }

    @Cacheable(value = ["products"], key = "#id")
    override fun findById(id: String): Product {
        logger.info("Getting product by id ${id}")

        val result = productRepository.findById(id)
            .orElseThrow { ProductNotFoundException("Product with ID $id not found") }

        return ProductMapper.entityToDomain(result)
    }

    override fun save(product: Product, user: User?): Product {
        if (user != null) {
            product.username = user.username
        }

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
        if (!productRepository.existsById(productId)) {
            throw ProductNotFoundException("Product with ID $productId not found")
        }

        productRepository.deleteById(productId)
        logger.info("The product ${productId} was successfully deleted")
    }

    private fun getTotalPages(pageSize: Int): Int {
        val totalElements = productRepository.count()
        return ((totalElements / pageSize) + if (totalElements % pageSize > 0) 1 else 0).toInt()
    }

    private fun findAllFilters(
        requestParam: FilterProductParamsDTO?,
        allProducts: MutableList<ProductEntity>
    ): List<ProductEntity> =
        allProducts.asSequence().filter { product ->
            requestParam?.name?.let { product.name.contains(it, ignoreCase = true) } ?: true &&
                    requestParam?.category?.let { product.category.contains(it, ignoreCase = true) } ?: true
        }.toList()

    private fun linkToProductController(products: List<Product>): Unit {
        for (item in products) {
            val withSelfRel = linkTo(ProductController::class.java).slash(item.id).withSelfRel()
            item.add(withSelfRel)
        }
    }

    override fun patchProduct(id: String, updates: Map<String, Any>): Product {
         try {
            val productDb = productRepository.findById(id).orElseThrow {
                ProductNotFoundException("Product with ID $id not found")
            }

            logger.info("Patching product with id $id")

            updates.forEach { (key, value) ->
                when (key) {
                    "name" -> productDb.name = value as? String ?: productDb.name
                    "price" -> productDb.price = value as? Double ?: productDb.price
                    "image" -> productDb.image = value as? String ?: productDb.image
                    "description" -> productDb.description = value as? String ?: productDb.description
                    "weight" -> productDb.weight = value as? Double ?: productDb.weight
                    "measure" -> productDb.measure = value as? String ?: productDb.measure
                    "color" -> productDb.color = value as? String ?: productDb.color
                    "category" -> {
                        val categoryDescription = value as? String
                        val category = Category.getCategoryByName(categoryDescription ?: "")
                        if (category != Category.UNKNOWN) {
                            productDb.category = category.name
                        }
                    }
                    "brand" -> productDb.brand = value as? String ?: productDb.brand
                }
            }

            productRepository.save(productDb)

            logger.info("The product ${productDb.id} was successfully patched")

            return ProductMapper.entityToDomain(productDb)
        } catch (e: ProductNotFoundException) {
            logger.error("Product not found: ${e.message}")
            throw e
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid field or type in patch request: ${e.message}")
            throw e
        } catch (e: Exception) {
            logger.error("An error occurred while patching the product: ${e.message}")
            throw RuntimeException("An error occurred while patching the product", e)
        }
    }
}

