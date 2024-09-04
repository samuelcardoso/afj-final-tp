package puc.products

import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductService(val productRepository: ProductRepository) {

    fun findAll(): List<Product> = productRepository.findAll()
    fun findById(id: String): Optional<Product> = productRepository.findById(id)
    fun save(product: Product): Product = productRepository.save(product)
}