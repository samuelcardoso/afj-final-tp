package puc.domain.products.services

import puc.application.dtos.FilterProductParamsDTO
import puc.application.dtos.PaginatedResponseDTO
import puc.domain.products.model.Product

interface IProductService {
    fun findAll(filterParams: FilterProductParamsDTO): PaginatedResponseDTO<Product>
    fun findById(id: String): Product?
    fun save(product: Product): Product
    fun delete(productId:String)
    fun update()
}