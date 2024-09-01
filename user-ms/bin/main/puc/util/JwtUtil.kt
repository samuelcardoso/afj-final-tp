package puc.util

import org.springframework.stereotype.Component
import puc.user.SessionToken

@Component
class JwtUtil {

    private val sampleJWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

    fun generateToken(sessionToken: SessionToken): String {
        return sampleJWT
    }

    fun validateToken(token: String): Boolean {
        return true
    }

}