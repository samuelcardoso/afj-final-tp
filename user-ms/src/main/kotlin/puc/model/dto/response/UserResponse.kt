package puc.model.dto.response

data class UserResponse(
    val username: String,
    val roles: Set<String>
)