package puc.controller

import jakarta.validation.Valid
import java.net.URI
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import puc.model.dto.request.*
import puc.model.dto.response.LoginResponse
import puc.model.dto.response.RefreshTokenResponse
import puc.model.dto.response.UserResponse
import puc.service.UserAppService

@RestController
@RequestMapping("/users")
class UserAppController(val userService: UserAppService) {

    private val ROUTE_ME = "/users/me"

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {
        val userDTOResponse = userService.register(request)
        val url = URI.create(ROUTE_ME)
        return ResponseEntity.created(url).body(userDTOResponse)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val loginResponse = userService.login(request.username, request.password)
        return ResponseEntity.ok(loginResponse)
    }

    @GetMapping("/me")
    fun getMe(): ResponseEntity<UserResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val userDetails = userService.getUserInfo(auth.name)
        return ResponseEntity.ok(userDetails)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(
        @Valid
        @RequestBody(required = true)
        request: RefreshTokenRequest
    ): ResponseEntity<RefreshTokenResponse> {
        val refreshToken = request.refreshToken
        val refreshTokenResponse = userService.refreshToken(refreshToken)
        return ResponseEntity.ok(refreshTokenResponse)
    }


    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid
        @RequestBody(required = true)
        request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val userResponse = userService.updateUser(id, request)
        return ResponseEntity.ok(userResponse)
    }

    @PatchMapping("/change-password")
    fun changePassword(
        @Valid
        @RequestBody(required = true)
        request: ChangePasswordRequest
    ): ResponseEntity<UserResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val userResponse = userService.changePassword(auth.name, request)
        return ResponseEntity.ok(userResponse)
    }
}
