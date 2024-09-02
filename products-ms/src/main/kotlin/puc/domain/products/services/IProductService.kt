package puc.domain.products.services

import puc.application.dtos.FilterProductParamsDTO
import puc.application.dtos.PaginatedResponseDTO
import puc.domain.products.model.Product

interface IProductService {
    fun findAll(filterParams: FilterProductParamsDTO): PaginatedResponseDTO<Product>
    fun findById(id: String): Product
    fun save(product: Product): Product
    fun update(product: Product, id: String): Product?
    fun delete(productId:String)
    fun patchProduct(id: String, updates: Map<String, Any>): Product
}
