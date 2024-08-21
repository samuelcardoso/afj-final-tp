package puc.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserAppRepository : JpaRepository<UserApp, Long> {
    fun findByUsername(username: String): UserApp?
}