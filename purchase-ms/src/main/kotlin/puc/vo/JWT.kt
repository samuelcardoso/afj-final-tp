package puc.vo

import org.springframework.web.client.HttpClientErrorException
import puc.dto.response.UserResponse
import puc.exceptions.ErrorCreatingJWTException
import puc.exceptions.ErrorTryingToConnectException
import puc.gateway.UserMsRestTemplate

class JWT private constructor(val token: String,
          val id: Long,
          val username: String,
          val roles: Set<String>){

    companion object {
        @JvmStatic
        fun create(token:String, userMsRestTemplate: UserMsRestTemplate): JWT {
            val userResponse : UserResponse
            try {
                userResponse = userMsRestTemplate.getMe(token).get()
            } catch (e : HttpClientErrorException) {
                throw ErrorCreatingJWTException("Failed to authenticate.")
            }

            if (userResponse.id == null) {
                throw ErrorCreatingJWTException("It wat not possible to create the object. ID not present in the response.");
            }

            return JWT(token, userResponse.id, userResponse.username, userResponse.roles);
        }
    }

}