package puc.products

import org.springframework.stereotype.Service

@Service
class ProductService(val productRepository: ProductRepository) {

    fun findAll(): List<Product> = productRepository.findAll()
    fun save(product: Product): Product = productRepository.save(product)
}