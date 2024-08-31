package puc.model.dto.request

import CpfCnpj
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Pattern

data class UpdateUserRequest(
    @field:Size(min = 1, message = "Must have at least one role")
    val roles: Set<@Pattern(
        regexp = "^(ROLE_USER|ROLE_USER_CLIENT)$",
        message = "Invalid role"
    ) String> = emptySet(),

    @field:Size(min = 11, max = 14)
    @CpfCnpj
    @field:NotBlank(message = "Document is required")
    val document: String,

    @field:NotBlank(message = "First name is required")
    @field:Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    @field:Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    val lastName: String,

    @field:NotBlank(message = "Email is required")
    @field:Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    val email: String,

    @field:Size(min = 8, max = 20, message = "Phone must be between 8 and 20 characters")
    @field:Nullable
    val phone: String?,
)