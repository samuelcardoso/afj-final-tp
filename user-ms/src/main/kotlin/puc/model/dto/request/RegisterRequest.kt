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
    val password: String,

    @field:NotBlank(message = "First name is required")
    @field:Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    @field:Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    val lastName: String,

    @field:NotBlank(message = "Email is required")
    @field:Size(min = 3, max = 50, message = "Email must be between 3 and 50 characters")
    val email: String,

    @field:NotBlank(message = "Phone is required")
    @field:Size(min = 3, max = 50, message = "Phone must be between 3 and 50 characters")
    val phone: String,

    @field:NotBlank(message = "Document is required")
    @field:Size(min = 3, max = 50, message = "Document must be between 3 and 50 characters")
    val document: String
)
