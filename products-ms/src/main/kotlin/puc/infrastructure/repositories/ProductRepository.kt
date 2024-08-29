package puc.infrastructure.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import puc.infrastructure.entities.ProductEntity

interface ProductRepository : MongoRepository<ProductEntity, String>{
    fun findByName(name: String): List<ProductEntity>
}