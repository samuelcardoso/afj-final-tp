package puc.products.`in`

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import puc.products.domain.IProductService
import puc.products.domain.Product

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
        @RequestBody incomingProduct: IncomingProduct,
        uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<Any>{
        val product = incomingProduct.toDomain()

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