package puc.domain.users.services

import puc.domain.users.model.User

interface IUserService {
    fun getLoggedUser(): User?
}