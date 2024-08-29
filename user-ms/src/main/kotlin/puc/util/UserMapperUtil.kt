package puc.util

import puc.model.dto.response.UserResponse
import puc.model.sql.UserApp

object UserMapperUtil {

    fun toUserDTO(userApp: UserApp): UserResponse {
        return UserResponse(
            username = userApp.username,
            roles = userApp.roles
        )
    }

    fun toUserApp(userResponse: UserResponse): UserApp {
        return UserApp(
            username = userResponse.username,
            password = "",
            roles = userResponse.roles.toMutableSet()
        )
    }
}
