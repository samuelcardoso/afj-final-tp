package puc.util

import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    private val secretKey = "mySecretKey"

    fun validateToken(token: String): Boolean {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
        return !claims.body.expiration.before(Date())
    }

    fun extractUserId(token: String): Long {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
        return claims.body.subject.toLong()
    }
}