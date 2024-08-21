package puc.products

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Product(
    @Id
    val id: String? = null,
    val name: String,
    val price: Double,
)