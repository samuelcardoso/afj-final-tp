package puc.model.dto.request

import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Pattern

data class UpdateUserRequest(
    @field:Size(min = 1, message = "Must have at least one role")
    val roles: Set<@Pattern(
        regexp = "^(ROLE_USER|ROLE_ADMIN)$",
        message = "Invalid role"
    ) String> = emptySet()
)