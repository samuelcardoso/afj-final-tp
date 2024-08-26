package puc.products.`in`

import org.springframework.web.bind.annotation.*
import puc.products.domain.Product
import puc.products.domain.ProductService

@RestController
@RequestMapping("/products")
class ProductController(val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): List<Product> = productService.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): Product? = productService.findById(id)

    @PostMapping
    fun postProduct(@RequestBody incomingProduct: IncomingProduct): Product{
        val product = incomingProduct.toDomain()

        return productService.save(product)
    }
}