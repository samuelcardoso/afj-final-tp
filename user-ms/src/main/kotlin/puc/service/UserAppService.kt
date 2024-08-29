package puc.service

import puc.model.dto.request.RegisterRequest
import puc.model.dto.response.LoginResponse
import puc.model.dto.response.UserResponse

interface UserAppService {
    fun register(request: RegisterRequest): UserResponse
    fun login(username: String, password: String): LoginResponse
    fun getUserInfo(username: String): UserResponse
}