package puc.model.dto.response

data class LoginResponse(
    val user: UserResponse,
    override val token: String,
    override val tokenType: String,
    override val tokenExpiresIn: Long,
    override val refreshToken: String
) : AbstractAuthResponse()