package puc.utils

import puc.application.dtos.ProductDTO
import puc.domain.products.model.Product
import java.time.format.DateTimeFormatter

class ProductConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun toDto(product: Product): ProductDTO {
        return ProductDTO(
            name = product.name,
            price = product.price,
            image =product.image,
            description = product.description,
            weight = product.weight,
            measure =product.measure,
            color = product.color,
            category = product.category,
            brand = product.brand
        )
    }

//    fun fromDto(productDTO: ProductDTO): Product {
//        return Product(
//            id = productDTO.id,
//            name = productDTO.name,
//            category = productDTO.category,
//            createdDate = LocalDateTime.parse(productDTO.createdDate, formatter)
//        )
//    }
}