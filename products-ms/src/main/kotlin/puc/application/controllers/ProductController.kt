package puc.application.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import puc.application.dtos.ProductDTO
import puc.domain.products.services.IProductService
import puc.domain.products.model.Product

@RestController
@RequestMapping("/products")
class ProductController(val productService: IProductService) {

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<Product>> {
        val response = productService.findAll()

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): Product? = productService.findById(id)

    @GetMapping("/{name}")
    fun getByName(@PathVariable name:String) : ResponseEntity<List<Product>>{
        val result = productService.findByName(name)

        return ResponseEntity.ok(result)
    }

    @PostMapping
    fun postProduct(
        @RequestBody productDTO: ProductDTO,
        uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<Any>{
        val product = productDTO.toDomain()

        val result =  productService.save(product)
        val location = uriComponentsBuilder.path("/products/{id}").buildAndExpand(result.id).toUri()
        return ResponseEntity.created(location).build()
    }

    @DeleteMapping("/{idProduct}")
    fun deleteProduct(@PathVariable idProduct:String) : ResponseEntity<Any>{
        productService.delete(idProduct)
        return ResponseEntity.noContent().build()
    }

}