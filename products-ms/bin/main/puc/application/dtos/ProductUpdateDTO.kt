package puc.application.dtos

import puc.domain.enums.Category

data class ProductUpdateDTO(
    val name: String? = null,
    val price: Double? = null,
    val description: String? = null,
    val weight: Double? = null,
    val measure: String? = null,
    val color: String? = null,
    val category: Category? = null,
    val brand: String? = null
)
