package puc.application.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import puc.application.dtos.ProductDTO
import puc.domain.products.services.IProductService
import puc.domain.products.model.Product
import puc.domain.products.services.GetAllProductsRequestParam
import puc.domain.mappers.ProductMapper

@RestController
@RequestMapping("/products")
class ProductController(val productService: IProductService) {

    @GetMapping
    fun getAllProducts(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) price: Double?,
        @RequestParam(required = false) description: String?,
        @RequestParam(required = false) weight: Double?,
        @RequestParam(required = false) measure: String?,
        @RequestParam(required = false) color: String?,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) brand: String?,
        ): ResponseEntity<List<Product>> {

        val param = GetAllProductsRequestParam(
            name = name,
            price = price,
            description = description,
            weight = weight,
            measure = measure,
            color = color,
            category = category,
            brand = brand,
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

        return ResponseEntity.created(location).build()
    }

    @DeleteMapping("/{idProduct}")
    fun deleteProduct(@PathVariable idProduct:String) : ResponseEntity<Any>{
        productService.delete(idProduct)
        return ResponseEntity.noContent().build()
    }

}