package puc.model.dto.response

data class RefreshTokenResponse(
    override val token: String = "",
    override val tokenType: String = "",
    override val tokenExpiresIn: Long = 0,
    override val refreshToken: String = ""
) : AbstractAuthResponse()