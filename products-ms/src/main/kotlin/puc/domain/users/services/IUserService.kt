package puc.domain.users.services

import puc.domain.users.model.User

interface IUserService {
    fun findById(id: Long): User?
    fun getMe(): User?
}