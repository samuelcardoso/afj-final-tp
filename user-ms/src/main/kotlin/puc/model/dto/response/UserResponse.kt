package puc.model.dto.response

import java.time.LocalDateTime

data class UserResponse(
    val id: Long?,
    val username: String,
    val roles: Set<String>,
    val document: String? = "",
    val email: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val phone: String? = "",
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
