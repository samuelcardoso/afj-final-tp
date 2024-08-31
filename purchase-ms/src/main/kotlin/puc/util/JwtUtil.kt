package puc.util

import org.springframework.stereotype.Component
import puc.gateway.UserMsRestTemplate

@Component
class JwtUtil(val userMsRestTemplate: UserMsRestTemplate) {

    fun validateToken(token: String): Boolean {
        return userMsRestTemplate.getMe(token).isPresent
    }

    fun extractUserId(token: String): Long {
        return 1
    }
}