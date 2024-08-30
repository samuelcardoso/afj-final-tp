package puc.stock.service

import puc.stock.resources.ProductResource

interface ProductService {
    fun findProductById(id: String): ProductResource
}