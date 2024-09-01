package puc.domain.products.model

import org.springframework.hateoas.RepresentationModel
import puc.domain.enums.Category
import java.io.Serializable
import java.time.LocalDateTime

data class Product(
    val id: String? = null,
    val name: String,
    val price: Double,
    val image:String,
    val description: String,
    val weight:Double,
    val measure:String,
    val color:String,
    val category: Category,
    val brand:String,
    val dataRegister: LocalDateTime? = null
) : Serializable, RepresentationModel<Product>(){
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(!price.isNaN()) { "Price must be a nan" }
        require(price > 0) { "Price must be greater than zero" }
        require(image.isNotBlank()) { "Image must not be blank" }
        require(description.isNotBlank()) { "Description must not be blank" }
        require(!weight.isNaN()) { "Weight must be a nan" }
        require(measure.isNotBlank()) { "Measure must not be blank" }
        require(color.isNotBlank()) { "Color must not be blank" }
        require(category != Category.UNKNOWN) { "Category must not be blank" }
        require(brand.isNotBlank()) { "Brand must not be blank" }
    }
}