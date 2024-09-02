package puc.controller
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import puc.exception.custom.*
import puc.model.dto.request.*
import puc.model.dto.response.LoginResponse
import puc.model.dto.response.RefreshTokenResponse
import puc.model.dto.response.UserResponse
import puc.service.UserAppService
import java.net.URI

@ExtendWith(MockitoExtension::class)
class UserAppControllerTest {

    @Mock
    private lateinit var userService: UserAppService

    @InjectMocks
    private lateinit var userAppController: UserAppController

    private lateinit var registerRequest: RegisterRequest
    private lateinit var userResponse: UserResponse
    private lateinit var loginRequest: LoginRequest
    private lateinit var loginResponse: LoginResponse
    private lateinit var refreshTokenRequest: RefreshTokenRequest
    private lateinit var refreshTokenResponse: RefreshTokenResponse
    private lateinit var updateUserRequest: UpdateUserRequest
    private lateinit var changePasswordRequest: ChangePasswordRequest

    @BeforeEach
    fun setUp() {
        registerRequest = RegisterRequest(
            username = "newuser",
            password = "StrongPassword123!",
            firstName = "New",
            lastName = "User",
            email = "newuser@example.com",
            phone = "1234567890",
            document = "12345678901"
        )

        userResponse = UserResponse(
            id = 1L,
            username = "newuser",
            roles = setOf("ROLE_USER"),
            document = "12345678901",
            email = "newuser@example.com",
            firstName = "New",
            lastName = "User",
            phone = "1234567890"
        )

        loginRequest = LoginRequest(
            username = "newuser",
            password = "StrongPassword123!"
        )

        loginResponse = LoginResponse(
            user = userResponse,
            token = "token",
            tokenType = "Bearer",
            tokenExpiresIn = 3600L,
            refreshToken = "refreshToken"
        )

        refreshTokenRequest = RefreshTokenRequest(
            refreshToken = "refreshToken"
        )

        refreshTokenResponse = RefreshTokenResponse(
            token = "exampleToken",
            tokenType = "Bearer",
            tokenExpiresIn = 3600L,
            refreshToken = "exampleRefreshToken"
        )

        updateUserRequest = UpdateUserRequest(
            roles = setOf("ROLE_USER"),
            document = "1234567890",
            firstName = "New",
            lastName = "User",
            email = "newuser@example.com",
            phone = "1234567890"
        )

        changePasswordRequest = ChangePasswordRequest(
            oldPassword = "StrongPassword123!",
            newPassword = "NewStrongPassword!456"
        )

        val principal = org.springframework.security.core.userdetails.User(
            "user",
            "pass",
            listOf(SimpleGrantedAuthority("ROLE_USER"))
        )
        val auth = UsernamePasswordAuthenticationToken(principal, null, listOf(SimpleGrantedAuthority("ROLE_USER")))

        SecurityContextHolder.getContext().authentication = auth
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `register should create a new user successfully`() {
        Mockito.`when`(userService.register(registerRequest)).thenReturn(userResponse)

        val response: ResponseEntity<UserResponse> = userAppController.register(registerRequest)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(URI.create("/users/me"), response.headers.location)
        assertEquals(userResponse, response.body)
    }

    @Test
    fun `register should throw UsernameAlreadyExistsException when username already exists`() {
        Mockito.`when`(userService.register(registerRequest)).thenThrow(UsernameAlreadyExistsException("User already exists with the username newuser"))

        val exception = org.junit.jupiter.api.assertThrows<UsernameAlreadyExistsException> {
            userAppController.register(registerRequest)
        }

        assertEquals("User already exists with the username newuser", exception.message)
    }

    @Test
    fun `login should return success for a user`() {
        Mockito.`when`(userService.login(loginRequest.username, loginRequest.password)).thenReturn(loginResponse)

        val response: ResponseEntity<LoginResponse> = userAppController.login(loginRequest)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(loginResponse, response.body)
    }

    @Test
    fun `login should throw UserNotFoundException when username no exists`() {
        Mockito.`when`(userService.login(loginRequest.username, loginRequest.password)).thenThrow(UserNotFoundException("User not found"))

        val exception = org.junit.jupiter.api.assertThrows<UserNotFoundException> {
            userAppController.login(loginRequest)
        }

        assertEquals("User not found", exception.message)
    }

    @Test
    fun `login should throw InvalidCredentialsException when password are wrong`() {
        Mockito.`when`(userService.login(loginRequest.username, loginRequest.password)).thenThrow(InvalidCredentialsException("Invalid credentials"))

        val exception = org.junit.jupiter.api.assertThrows<InvalidCredentialsException> {
            userAppController.login(loginRequest)
        }

        assertEquals("Invalid credentials", exception.message)
    }


    @Test
    fun `getMe should return user infos`() {
        Mockito.`when`(userService.getUserInfo("user")).thenReturn(userResponse)

        val response: ResponseEntity<UserResponse> = userAppController.getMe()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(userResponse, response.body)
    }

    @Test
    fun `getMe should throw UserNotFoundException when username no exists`() {
        Mockito.`when`(userService.getUserInfo("user")).thenThrow(UserNotFoundException("User not found"))

        val exception = org.junit.jupiter.api.assertThrows<UserNotFoundException> {
            userAppController.getMe()
        }

        assertEquals("User not found", exception.message)
    }

    @Test
    fun `refreshToken should return a new token`() {
        Mockito.`when`(userService.refreshToken(refreshTokenRequest.refreshToken)).thenReturn(refreshTokenResponse)

        val response: ResponseEntity<RefreshTokenResponse> = userAppController.refreshToken(refreshTokenRequest)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(refreshTokenResponse, response.body)
    }

    @Test
    fun `refreshToken should throw InvalidCredentialsException when RefreshToken are invalid`() {
        Mockito.`when`(userService.refreshToken(refreshTokenRequest.refreshToken)).thenThrow(InvalidCredentialsException("Invalid refresh token"))

        val exception = org.junit.jupiter.api.assertThrows<InvalidCredentialsException> {
            userAppController.refreshToken(refreshTokenRequest)
        }

        assertEquals("Invalid refresh token", exception.message)
    }

    @Test
    fun `refreshToken should throw UserNotFoundException when username no exists`() {
        Mockito.`when`(userService.refreshToken(refreshTokenRequest.refreshToken)).thenThrow(UserNotFoundException("User not found"))

        val exception = org.junit.jupiter.api.assertThrows<UserNotFoundException> {
            userAppController.refreshToken(refreshTokenRequest)
        }

        assertEquals("User not found", exception.message)
    }

    @Test
    fun `updateUser should update the user infos`() {
        Mockito.`when`(userService.updateUser(1L, updateUserRequest, "user", updateUserRequest.roles)).thenReturn(userResponse)

        val response: ResponseEntity<UserResponse> = userAppController.updateUser(1L, updateUserRequest)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(userResponse, response.body)
    }

    @Test
    fun `updateUser should throw UserNotFoundException when username no exists`() {
        Mockito.`when`(userService.updateUser(1L, updateUserRequest, "user", updateUserRequest.roles)).thenThrow(UserNotFoundException("User not found"))

        val exception = org.junit.jupiter.api.assertThrows<UserNotFoundException> {
            userAppController.updateUser(1L, updateUserRequest)
        }

        assertEquals("User not found", exception.message)
    }

    @Test
    fun `updateUser should throw InvalidRoleException when role is invalid`() {
        Mockito.`when`(userService.updateUser(1L, updateUserRequest, "user", updateUserRequest.roles)).thenThrow(
            InvalidRoleException("User with ROLE_USER can only update themselves")
        )

        val exception = org.junit.jupiter.api.assertThrows<InvalidRoleException> {
            userAppController.updateUser(1L, updateUserRequest)
        }

        assertEquals("User with ROLE_USER can only update themselves", exception.message)
    }

    @Test
    fun `updateUser should throw InvalidRoleException when new role is invalid`() {
        Mockito.`when`(userService.updateUser(1L, updateUserRequest, "user", updateUserRequest.roles)).thenThrow(
            InvalidRoleException("User with ROLE_USER can only have ROLE_USER")
        )

        val exception = org.junit.jupiter.api.assertThrows<InvalidRoleException> {
            userAppController.updateUser(1L, updateUserRequest)
        }

        assertEquals("User with ROLE_USER can only have ROLE_USER", exception.message)
    }

    @Test
    fun `updateUser should throw UserEmailAlreadyExistsException when new email already exist`() {
        Mockito.`when`(userService.updateUser(1L, updateUserRequest, "user", updateUserRequest.roles)).thenThrow(
            UserEmailAlreadyExistsException("User already exists with the email %s".format(updateUserRequest.email))
        )

        val exception = org.junit.jupiter.api.assertThrows<UserEmailAlreadyExistsException> {
            userAppController.updateUser(1L, updateUserRequest)
        }

        assertEquals("User already exists with the email %s".format(updateUserRequest.email), exception.message)
    }

    @Test
    fun `changePassword should update the user password`() {
        Mockito.`when`(userService.changePassword("user", changePasswordRequest)).thenReturn(userResponse)

        val response: ResponseEntity<UserResponse> = userAppController.changePassword(changePasswordRequest)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(userResponse, response.body)
    }

    @Test
    fun `changePassword should throw UserNotFoundException when username no exists`() {
        Mockito.`when`(userService.changePassword("user", changePasswordRequest)).thenThrow(UserNotFoundException("User not found"))

        val exception = org.junit.jupiter.api.assertThrows<UserNotFoundException> {
            userAppController.changePassword(changePasswordRequest)
        }

        assertEquals("User not found", exception.message)
    }

    @Test
    fun `changePassword should throw InvalidCredentialsException when old password do not match`() {
        Mockito.`when`(userService.changePassword("user", changePasswordRequest)).thenThrow(
            InvalidCredentialsException("Invalid old password")
        )

        val exception = org.junit.jupiter.api.assertThrows<InvalidCredentialsException> {
            userAppController.changePassword(changePasswordRequest)
        }

        assertEquals("Invalid old password", exception.message)
    }
}