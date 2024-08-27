package puc.util

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import puc.user.SessionToken
import java.util.*

@Component
class JwtUtil {
    private var key = Jwts.SIG.HS256.key().build()
    private val expiration = 86400000 // 1 day

    fun generateToken(sessionToken: SessionToken): String {
        return Jwts.builder()
            .subject(sessionToken.username)
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)

            return true
        } catch (e: JwtException) {
            //don't trust the JWT!
        }

        return false
    }
}