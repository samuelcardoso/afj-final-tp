package puc.infrastructure.filters

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.server.ResponseStatusException
import puc.domain.users.services.UserService
import puc.infrastructure.config.HeaderContext
import java.time.LocalDate

@Component
class AuthenticationFilter(val userService: UserService, val objectMapper: ObjectMapper): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val headerValue = request.getHeader("Authorization")
        HeaderContext.setHeader(headerValue)

        val user = userService.getMe();

        if (user != null) {
            val roles = user.roles?.map { SimpleGrantedAuthority(it) }
            val authentication = UsernamePasswordAuthenticationToken(user, null, roles)
            SecurityContextHolder.getContext().authentication = authentication
            filterChain.doFilter(request, response);
        } else {
            response.characterEncoding = "UTF-8"
            response.status = HttpStatus.UNAUTHORIZED.value()
            val problemDetail: ProblemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Request with invalid token")
            problemDetail.title = "Unauthorized"
            problemDetail.setProperty("timestamp", LocalDate.now())
            response.writer.println(objectMapper.writeValueAsString(problemDetail))
        }

        HeaderContext.clear()
    }
}