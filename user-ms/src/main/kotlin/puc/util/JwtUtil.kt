package puc.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import puc.user.User
import java.util.*

@Component
class JwtUtil {

    private val secretKey = "mySecretKey"

    fun generateToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.id.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 86400000)) // 1 dia
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
        return !claims.body.expiration.before(Date())
    }

    fun extractUserId(token: String): Long {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
        return claims.body.subject.toLong()
    }
}