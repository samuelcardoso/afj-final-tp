package puc.domain.products.services

import puc.domain.products.model.Product
import puc.domain.users.model.User

interface IProductService {
    fun findAll(requestParam: GetAllProductsRequestParam?): List<Product>
    fun findById(id: String): Product?
    fun save(product: Product, user: User?): Product
    fun delete(productId:String)
    fun update()
}