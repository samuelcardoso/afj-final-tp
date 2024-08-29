package puc.model.sql

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

private val DEFAULT_ROLE = "ROLE_USER"

@Entity
data class UserApp(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "username", unique = true)
    val username: String,

    @Column(name = "password")
    var password: String,

    @ElementCollection(fetch = FetchType.EAGER)
    var roles: MutableSet<String> = mutableSetOf(DEFAULT_ROLE),

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
