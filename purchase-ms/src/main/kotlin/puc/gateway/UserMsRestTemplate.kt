package puc.gateway

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import puc.config.RestTemplateConfig
import puc.dto.response.UserResponse
import java.util.Optional

@Component
class UserMsRestTemplate(val restTemplateConfig: RestTemplateConfig) {

    @Value("\${user-ms.router.me}")
    lateinit var ROUTE_ME: String

    fun getMe(token: String): Optional<UserResponse> {
        val headers = HttpHeaders().apply {
            set("Authorization", token)
        }

        val entity = HttpEntity<String>(headers)
        val response: ResponseEntity<UserResponse> = restTemplateConfig.restTemplate().exchange(ROUTE_ME, HttpMethod.GET, entity, UserResponse::class.java)

        return Optional.ofNullable(response.body)
    }

}