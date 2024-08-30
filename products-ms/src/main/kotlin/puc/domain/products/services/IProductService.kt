package puc.domain.products.services

import puc.domain.products.model.Product

interface IProductService {
    fun findAll(requestParam: GetAllProductsRequestParam?): List<Product>
    fun findById(id: String): Product?
    fun save(product: Product): Product
    fun update(product: Product, id: String): Product?
    fun delete(productId:String)
    fun update()
}