package puc.products.domain

interface IProductService {
    fun findAll(filterParams: FilterProductParams): PaginatedResponse<Product>
    fun findById(id: String): Product?
    fun save(product: Product): Product
    fun delete(productId:String)
    fun update()
}