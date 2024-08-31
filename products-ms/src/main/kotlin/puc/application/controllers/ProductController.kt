package puc.application.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import puc.application.dtos.ProductDTO
import puc.domain.products.services.IProductService
import puc.domain.products.model.Product
import puc.application._shared.ProductMapper
import puc.application.dtos.FilterProductParamsDTO
import puc.application.dtos.PaginatedResponseDTO
import puc.application._shared.ProductEventPublisher

@RestController
@RequestMapping("/products")
class ProductController(
    val productService: IProductService,
    val productEventPublisher: ProductEventPublisher
) {

    @GetMapping
    fun getAllProducts(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?,

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
    fun getById(@PathVariable id: String): Product? {
        return productService.findById(id)
    }

    @PostMapping
    fun postProduct(@RequestBody productDTO: ProductDTO,
                    uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<Any>{
        val result =  productService.save(ProductMapper.dtoToDomain(productDTO))
        val location = uriComponentsBuilder
            .path("/products/{id}")
            .buildAndExpand(result.id).toUri()

        productEventPublisher.publishProductRegisteredEvent(result)

        return ResponseEntity.created(location).build()
    }

    @PutMapping("/{id}")
    fun putProduct( @PathVariable id: String,
                    @RequestBody productDTO: ProductDTO,
                    uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<Any>{
        val result =  productService.update(ProductMapper.dtoToDomain(productDTO), id)
        val location = uriComponentsBuilder
            .path("/products/{id}")
            .buildAndExpand(result?.id).toUri()

        return ResponseEntity.created(location).build()
    }

    @DeleteMapping("/{idProduct}")
    fun deleteProduct(@PathVariable idProduct:String) : ResponseEntity<Any>{
        productService.delete(idProduct)

        productEventPublisher.publishProductDeletedEvent(idProduct)

        return ResponseEntity.noContent().build()
    }

}