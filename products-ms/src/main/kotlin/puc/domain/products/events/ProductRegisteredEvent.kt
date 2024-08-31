package puc.domain.products.events

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductRegisteredEvent(
    @JsonProperty("productId") val productId: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("price") val price: Double
)