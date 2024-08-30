package puc.domain.mappers

import puc.application.dtos.ProductDTO
import puc.domain.enums.Category
import puc.domain.products.model.Product
import puc.infrastructure.entities.ProductEntity

object ProductMapper {

//    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun entityToDomain(entity: ProductEntity): Product {
        return Product(
            id = entity.id,
            name = entity.name,
            dataRegister = entity.dataRegister,
            brand = entity.brand,
            price = entity.price,
            color = entity.color,
            image = entity.image,
            weight = entity.weight,
            description = entity.description,
            category =  Category.fromValue(entity.category),
            measure = entity.measure,
            userId = entity.userId,
            username = entity.username
        )
    }

    fun dtoToDomain(dto: ProductDTO): Product {
        return Product(
            weight = dto.weight,
            category = dto.category,
            image = dto.image,
            description = dto.description,
            measure = dto.measure,
            brand = dto.brand,
            color = dto.color,
            name = dto.name,
            price = dto.price,
            userId = dto.userId
        )
    }
}