package puc.model.dto.request

import jakarta.validation.constraints.NotBlank
import puc.annotation.StrongPassword

data class ChangePasswordRequest(
    @field:NotBlank(message = "New password is required")
    val oldPassword: String,

    @field:NotBlank(message = "New password is required")
    @StrongPassword
    val newPassword: String
)
