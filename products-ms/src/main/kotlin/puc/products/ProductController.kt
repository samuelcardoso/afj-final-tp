package puc.products

import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/products")
class ProductController(val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): List<Product> = productService.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): Optional<Product> = productService.findById(id)

    @PostMapping
    fun postProduct(@RequestBody product: Product): Product = productService.save(product)
}