package puc.model.dto.response

data class LoginResponse(
    val token: String,
    val expiresIn: Long,
    val user: UserResponse
)