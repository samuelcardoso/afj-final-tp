package puc.repository

import org.springframework.data.jpa.repository.JpaRepository
import puc.model.sql.UserApp

interface UserAppRepository : JpaRepository<UserApp, Long> {
    fun findByUsername(username: String): UserApp?
    fun findByEmail(email: String): UserApp?
    fun findByDocument(document: String): UserApp?
}
