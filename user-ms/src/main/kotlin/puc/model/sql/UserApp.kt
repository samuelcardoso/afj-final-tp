package puc.model.sql

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

private val DEFAULT_ROLE = "ROLE_USER"

@Entity
data class UserApp(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "username", unique = true)
    @NotBlank
    val username: String,

    @Column(name = "password")
    var password: String,

    @ElementCollection(fetch = FetchType.EAGER)
    var roles: MutableSet<String> = mutableSetOf(DEFAULT_ROLE),

    @Column(name = "document", length = 14, unique = true, nullable = true)
    @Nullable
    var document: String? = null,

    @Column(name ="first_name", length = 50)
    @Nullable
    var firstName: String?,

    @Column(name ="last_name", length = 50)
    @Nullable
    var lastName: String?,

    @Column(name = "email", unique = true, nullable = true)
    @Email
    @Nullable
    var email: String? = null,

    @Column(name = "phone", length = 20)
    @Nullable
    var phone: String?,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null

){
    @PrePersist
    fun prePersist() {
        updatedAt = LocalDateTime.now()
    }
}
