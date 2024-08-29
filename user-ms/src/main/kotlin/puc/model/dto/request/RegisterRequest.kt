package puc.model.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import puc.annotation.StrongPassword

data class RegisterRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @StrongPassword
    val password: String
)
