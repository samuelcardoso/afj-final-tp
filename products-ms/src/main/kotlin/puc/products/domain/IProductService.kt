package puc.products.domain

interface IProductService {
    fun findAll(): List<Product>
    fun findById(id: String): Product?
    fun findByName(name: String): List<Product>
    fun save(product: Product): Product
    fun delete(productId:String)
    fun update()
}