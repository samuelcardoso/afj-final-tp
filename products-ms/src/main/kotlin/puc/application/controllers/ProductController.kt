package puc.application.controllers

import jakarta.annotation.security.RolesAllowed
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import puc.application._shared.ProductEventPublisher
import puc.application.dtos.FilterProductParamsDTO
import puc.application.dtos.PaginatedResponseDTO
import puc.application.dtos.ProductDTO
import puc.domain.products.mappers.ProductMapper
import puc.domain.products.model.Product
import puc.domain.products.services.IProductService
import puc.domain.users.model.User

@RestController
@RequestMapping("/products")
class ProductController(
    val productService: IProductService,
    val productEventPublisher: ProductEventPublisher
) {

    @GetMapping
    @Operation(summary = "List all products", description = "Returns a list of all available products")
    fun getAllProducts(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?
    ): ResponseEntity<PaginatedResponseDTO<Product>> {
        val param = FilterProductParamsDTO(
            name = name,
            category = category,
            page = page,
            pageSize = pageSize
        )

        val response = productService.findAll(param)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search for product by ID", description = "Returns a product by its ID")
    fun getById(
        @PathVariable id: String
    ): ResponseEntity<Any> {
        val product = productService.findById(id)
        return ResponseEntity.ok(mapOf("product" to product))
    }

    @PostMapping
    @RolesAllowed("USER")
    @Operation(summary = "Create a new product", description = "Creates a new product and returns the created product")
    fun postProduct(@RequestBody productDTO: ProductDTO,
                    @AuthenticationPrincipal user: User,
                    uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<Any>{
        val result =  productService.save(ProductMapper.dtoToDomain(productDTO), user)
        val location = uriComponentsBuilder
            .path("/products/{id}")
            .buildAndExpand(result.id).toUri()

        productEventPublisher.publishProductRegisteredEvent(result)

        return ResponseEntity.created(location).body(result)
    }

    @PutMapping("/{id}")
    @RolesAllowed("USER")
    @Operation(summary = "Update an existing product", description = "Updates a product by its ID and returns the updated product")
    fun putProduct( @PathVariable id: String,
                    @RequestBody productDTO: ProductDTO,
                    uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<Any>{
        val result =  productService.update(ProductMapper.dtoToDomain(productDTO), id)
        val location = uriComponentsBuilder
            .path("/products/{id}")
            .buildAndExpand(result?.id).toUri()

        return ResponseEntity.created(location).body(result)
    }

    @DeleteMapping("/{idProduct}")
    @RolesAllowed("USER")
    @Operation(summary = "Delete a product", description = "Remove a product by its ID")
    fun deleteProduct(@PathVariable idProduct:String) : ResponseEntity<Any>{
        productService.delete(idProduct)
        productEventPublisher.publishProductDeletedEvent(idProduct)
        return ResponseEntity.ok(mapOf("message" to "Product deleted successfully"))
    }

    @PatchMapping("/{id}")
    @RolesAllowed("USER")
    @Operation(summary = "Patch an existing product", description = "Update any product attribute and returns the updated product")
    fun patchProduct(@PathVariable id: String, @RequestBody updates: Map<String, Any>): ResponseEntity<Any> {
        val updatedProduct = productService.patchProduct(id, updates)
        return ResponseEntity.ok(updatedProduct)
    }
}