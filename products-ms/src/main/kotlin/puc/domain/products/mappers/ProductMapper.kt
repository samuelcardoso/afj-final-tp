package puc.domain.products.mappers

import puc.application.dtos.ProductDTO
import puc.domain.enums.Category
import puc.domain.products.model.Product
import puc.infrastructure.entities.ProductEntity

object ProductMapper {

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
            category =  Category.getCategoryByName(entity.category),
            measure = entity.measure,
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
        )
    }

    fun domainToEntity(product: Product, productEntity: ProductEntity) {
        productEntity.name = product.name
        productEntity.brand = product.brand
        productEntity.price = product.price
        productEntity.color = product.color
        productEntity.image = product.image
        productEntity.weight = product.weight
        productEntity.description = product.description
        productEntity.category =  product.category.name
        productEntity.measure = product.measure
    }
}