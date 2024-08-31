package puc.model.dto.response

data class UserResponse(
    val id: Long?,
    val username: String,
    val roles: Set<String>
)
