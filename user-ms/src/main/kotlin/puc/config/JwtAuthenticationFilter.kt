package puc.config

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.Objects
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import puc.exception.custom.JwtTokenExpiredException
import puc.exception.custom.JwtTokenMalformedException
import puc.util.JwtUtil

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private val AUTHORIZATION_HEADER = "Authorization"
    private val BEARER_PREFIX = "Bearer "

    private val MESSAGE_ERROR = "JWT Token is malformed"
    private val MESSAGE_ERROR_EXPIRED = "JWT Token has expired"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(AUTHORIZATION_HEADER)
        if (isTokenPresent(authHeader)) {
            val jwtToken = extractToken(authHeader)
            try {
                val username = jwtUtil.getClaimsFromToken(jwtToken).subject
                if (isUsernameValid(username)) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    if (jwtUtil.validateToken(jwtToken)) {
                        setAuthentication(userDetails, request)
                    }
                }
            } catch (ex: ExpiredJwtException) {
                throw JwtTokenExpiredException(MESSAGE_ERROR_EXPIRED)
            } catch (ex: Exception) {
                throw JwtTokenMalformedException(MESSAGE_ERROR)
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun isTokenPresent(authHeader: String?): Boolean {
        return authHeader != null && StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)
    }

    private fun extractToken(authHeader: String): String {
        return authHeader.substring(7)
    }

    private fun isUsernameValid(username: String?): Boolean {
        return Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().authentication)
    }

    private fun setAuthentication(userDetails: UserDetails, request: HttpServletRequest) {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.authorities
        )
        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authenticationToken
    }
}