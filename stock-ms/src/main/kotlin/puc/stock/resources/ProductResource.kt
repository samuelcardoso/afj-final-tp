package puc.stock.resources

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductResource(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("price")
    val price: Double
)