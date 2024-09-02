package puc.controller
import java.net.URI
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
import puc.exception.custom.InvalidCredentialsException
import puc.exception.custom.UserNotFoundException
import puc.exception.custom.UsernameAlreadyExistsException
import puc.model.dto.request.LoginRequest
import puc.model.dto.request.RefreshTokenRequest
import puc.model.dto.request.RegisterRequest
import puc.model.dto.response.LoginResponse
import puc.model.dto.response.RefreshTokenResponse
import puc.model.dto.response.UserResponse
import puc.service.UserAppService

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
}