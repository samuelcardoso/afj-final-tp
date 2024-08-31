package puc.util

import puc.model.dto.response.UserResponse
import puc.model.sql.UserApp

object UserMapperUtil {

    fun toUserDTO(userApp: UserApp): UserResponse {
        return UserResponse(
            id = userApp.id,
            username = userApp.username,
            roles = userApp.roles,
            document = userApp.document,
            email = userApp.email,
            firstName = userApp.firstName,
            lastName = userApp.lastName,
            phone = userApp.phone,
            createdAt = userApp.createdAt,
            updatedAt = userApp.updatedAt
        )
    }

    fun toUserApp(userResponse: UserResponse): UserApp {
        return UserApp(
            id = userResponse.id,
            username = userResponse.username,
            password = "",
            roles = userResponse.roles.toMutableSet(),
            document = userResponse.document ?: "",
            email = userResponse.email ?: "",
            phone = userResponse.phone ?: "",
            firstName = userResponse.firstName ?: "",
            lastName = userResponse.lastName ?: "",
        )
    }
}
