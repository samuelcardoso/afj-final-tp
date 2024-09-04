package puc.domain.users.model

import java.util.*

data class User (
    val id: Long,
    val username: String,
    val roles: Set<String>?
) {
}