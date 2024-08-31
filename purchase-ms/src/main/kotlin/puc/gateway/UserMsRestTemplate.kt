package puc.gateway

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.stereotype.Component
import puc.config.RestTemplateConfig
import puc.dto.response.UserResponse
import java.util.Optional

@Component
class UserMsRestTemplate(val restTemplateConfig: RestTemplateConfig) {

    @Value("\${base-user-ms.router}")
    lateinit var BASE_ROUTE: String

    fun createHttpEntity(token: String): HttpEntity<String> {
        val headers = HttpHeaders().apply {
            set("Authorization", token)
        }
        return HttpEntity(headers)
    }

    fun <T> executeRequest(
        restTemplate: RestTemplate,
        route: String,
        httpMethod: HttpMethod,
        entity: HttpEntity<*>,
        responseType: Class<T>
    ): Optional<T> {
        val response: ResponseEntity<T> = restTemplate.exchange(route, httpMethod, entity, responseType)
        return Optional.ofNullable(response.body)
    }

    fun getMe(token: String): Optional<UserResponse> {
        val route = BASE_ROUTE + "me"
        val entity = createHttpEntity(token)
        return executeRequest(restTemplateConfig.restTemplate(), route, HttpMethod.GET, entity, UserResponse::class.java)
    }
}
