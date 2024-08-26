package puc.products.out.database

import org.springframework.data.mongodb.repository.MongoRepository
import puc.products.domain.Product

interface ProductRepository : MongoRepository<Product, String>