package puc.model.dto.response

abstract class AbstractAuthResponse {
    abstract val token: String
    abstract val tokenType: String
    abstract val tokenExpiresIn: Long
    abstract val refreshToken: String
}