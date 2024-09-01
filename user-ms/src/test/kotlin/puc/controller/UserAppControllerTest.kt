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
import puc.exception.custom.UsernameAlreadyExistsException
import puc.model.dto.request.RegisterRequest
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
}