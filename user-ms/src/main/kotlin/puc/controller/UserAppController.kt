package puc.controller

import ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import java.net.URI
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import puc.model.dto.request.ChangePasswordRequest
import puc.model.dto.request.LoginRequest
import puc.model.dto.request.RefreshTokenRequest
import puc.model.dto.request.RegisterRequest
import puc.model.dto.request.UpdateUserRequest
import puc.model.dto.response.LoginResponse
import puc.model.dto.response.RefreshTokenResponse
import puc.model.dto.response.UserResponse
import puc.service.UserAppService

@RestController
@RequestMapping("/users")
class UserAppController(val userService: UserAppService) {

    private val ROUTE_ME = "/users/me"

    @Operation(summary = "Register a new user", description = "Creates a new user in the system")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "User created successfully", content = [Content(schema = Schema(implementation = UserResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid input", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {
        val userDTOResponse = userService.register(request)
        val url = URI.create(ROUTE_ME)
        return ResponseEntity.created(url).body(userDTOResponse)
    }

    @Operation(summary = "Login a user", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Login successful", content = [Content(schema = Schema(implementation = LoginResponse::class))]),
        ApiResponse(responseCode = "401", description = "Invalid credentials", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val loginResponse = userService.login(request.username, request.password)
        return ResponseEntity.ok(loginResponse)
    }

    @Operation(summary = "Get current user info", description = "Retrieves information about the currently authenticated user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User info retrieved successfully", content = [Content(schema = Schema(implementation = UserResponse::class))]),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/me")
    fun getMe(): ResponseEntity<UserResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val userDetails = userService.getUserInfo(auth.name)
        return ResponseEntity.ok(userDetails)
    }

    @Operation(summary = "Refresh JWT token", description = "Refreshes the JWT token using a refresh token")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = [Content(schema = Schema(implementation = RefreshTokenResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid refresh token", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
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

    @Operation(summary = "Update user info", description = "Updates the information of an existing user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User updated successfully", content = [Content(schema = Schema(implementation = UserResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid input", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "User not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid
        @RequestBody(required = true)
        request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val authUsername = auth.name
        val authRoles = auth.authorities.map { it.authority }.toSet()
        val userResponse = userService.updateUser(id, request, authUsername, authRoles)
        return ResponseEntity.ok(userResponse)
    }

    @Operation(summary = "Change user password", description = "Changes the password of the currently authenticated user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Password changed successfully", content = [Content(schema = Schema(implementation = UserResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid input", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
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