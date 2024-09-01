package puc.util

import org.springframework.stereotype.Component

@Component
class JwtUtil {

    private val secretKey = "mySecretKey"

    fun validateToken(token: String): Boolean {
        return true
    }

    fun extractUserId(token: String): Long {
        return 1
    }
}