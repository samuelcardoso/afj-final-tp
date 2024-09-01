package puc.vo

import puc.gateway.UserMsRestTemplate

class JWT private constructor(val token: String,
          val id: Long,
          val username: String,
          val roles: Set<String>){

    companion object {
        @JvmStatic
        fun create(token:String, userMsRestTemplate: UserMsRestTemplate): JWT {
            val userResponse = userMsRestTemplate.getMe(token).orElseThrow{ RuntimeException("Failed to authenticate.") }
            if (userResponse.id == null) {
                throw RuntimeException("It wat not possible to create the object. ID not present in the response.");
            }
            return JWT(token, userResponse.id, userResponse.username, userResponse.roles);
        }
    }

}