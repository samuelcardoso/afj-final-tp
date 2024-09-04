package puc.util

import org.springframework.stereotype.Component
import puc.gateway.UserMsRestTemplate

@Component
class JwtUtil(val userMsRestTemplate: UserMsRestTemplate) {

    fun getUserId(token: String): Long {
        val userResponse = userMsRestTemplate.getMe(token).orElseThrow{ RuntimeException("Failed to authenticate.") }
        return userResponse.id ?: throw RuntimeException("ID not present in the response.");
    }
}