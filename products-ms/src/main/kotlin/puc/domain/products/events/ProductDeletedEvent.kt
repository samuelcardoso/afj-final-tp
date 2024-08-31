package puc.domain.products.events

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductDeletedEvent(
    @JsonProperty("productId") val productId: String
)
