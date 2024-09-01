package puc.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secret: String
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    private val ROLES_KEY = "roles"
    private val REFRESH_TOKEN_KEY = "refresh"

    fun generateToken(username: String, roles: Set<String>): String {
        val now = Date()
        val expiryDate = Date(now.time + 1000 * 60 * 60 * 10) // 10 hours validity

        return Jwts.builder()
            .subject(username)
            .claim(ROLES_KEY, roles)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun generateRefreshToken(username: String, roles: Set<String>): String {
        val now = Date()
        val expiryDate = Date(now.time + 1000L * 60 * 60 * 24 * 30) // 30 dias de validade

        return Jwts.builder()
            .subject(username)
            .claim(ROLES_KEY, roles)
            .claim(REFRESH_TOKEN_KEY, true)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = getClaimsFromToken(token)
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun getExpirationTime(token: String): Long {
        val claims = getClaimsFromToken(token)
        return claims.expiration.time
    }

    fun getUsernameFromToken(token: String): String {
        val claims = getClaimsFromToken(token)
        return claims.subject
    }

    fun validateRefreshToken(refreshToken: String): Boolean {
        return try {
            val claims = getClaimsFromToken(refreshToken)
            !claims.expiration.before(Date()) && claims[REFRESH_TOKEN_KEY] == true
        } catch (e: Exception) {
            false
        }
    }
}