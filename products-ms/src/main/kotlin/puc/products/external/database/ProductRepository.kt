package puc.products.external.database

import org.springframework.data.mongodb.repository.MongoRepository

interface ProductRepository : MongoRepository<ProductEntity, String>{
    fun findByName(name: String): List<ProductEntity>
}