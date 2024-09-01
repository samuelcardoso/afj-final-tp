package puc.user

import jakarta.persistence.*

@Entity
data class UserApp(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val username: String,
    val password: String
)

data class SessionToken(val username: String)