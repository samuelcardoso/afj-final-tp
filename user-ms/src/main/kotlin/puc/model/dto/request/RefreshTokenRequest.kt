package puc.model.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest @JsonCreator constructor(
    @JsonProperty("refreshToken")
    @field:NotBlank(message = "refreshToken is required")
    val refreshToken: String
)