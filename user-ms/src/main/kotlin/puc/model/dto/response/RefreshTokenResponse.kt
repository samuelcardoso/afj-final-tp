package puc.model.dto.response

data class RefreshTokenResponse(
    val token: String,
    val typeOfToken: String,
    val expiresIn: Long,
    val refreshToken: String
)
