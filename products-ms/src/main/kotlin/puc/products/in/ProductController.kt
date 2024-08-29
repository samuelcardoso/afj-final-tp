package puc.products.`in`

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import puc.products.domain.GetAllProductsRequestParam
import puc.products.domain.IProductService
import puc.products.domain.PaginatedResponse
import puc.products.domain.Product

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
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?,

        ): ResponseEntity<PaginatedResponse<Product>> {

        val param = GetAllProductsRequestParam(
            name = name,
            price = price,
            description = description,
            weight = weight,
            measure = measure,
            color = color,
            category = category,
            brand = brand,
            page = page,
            pageSize = pageSize
        )

        val response = productService.findAll(param)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): Product? = productService.findById(id)


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