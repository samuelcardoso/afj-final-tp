package puc.products

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): List<Product> = productService.findAll()

    @PostMapping
    fun postProduct(@RequestBody product: Product): Product = productService.save(product)
}