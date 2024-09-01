package puc.service.impl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import puc.exception.custom.UsernameAlreadyExistsException
import puc.model.dto.request.RegisterRequest
import puc.model.dto.response.UserResponse
import puc.model.sql.UserApp
import puc.repository.UserAppRepository
import puc.util.JwtUtil
import puc.util.UserMapperUtil

class UserAppServiceImplTest {

    private val userRepository: UserAppRepository = mock()
    private val jwtUtil: JwtUtil = mock()
    private val userAppService = UserAppServiceImpl(userRepository, jwtUtil)

    @Test
    fun `register should create a new user successfully`() {
        val request = RegisterRequest(
            username = "newuser",
            password = "StrongPassword123!",
            firstName = "New",
            lastName = "User",
            email = "newuser@example.com",
            phone = "1234567890",
            document = "12345678901"
        )

        val user = UserMapperUtil.toUserApp(
            UserResponse(
                id = null,
                username = "newuser",
                roles = setOf("ROLE_USER"),
                document = "12345678901",
                email = "newuser@example.com",
                firstName = "New",
                lastName = "User",
                phone = "1234567890"
            )
        )

        whenever(userRepository.save(Mockito.any(UserApp::class.java))).thenReturn(user)

        val response = userAppService.register(request)

        assertNotNull(response)
        assertEquals("newuser", response.username)
    }

    @Test
    fun `register should throw UsernameAlreadyExistsException when username already exists`() {
        val request = RegisterRequest(
            username = "existinguser",
            password = "StrongPassword123!",
            firstName = "Existing",
            lastName = "User",
            email = "existinguser@example.com",
            phone = "1234567890",
            document = "12345678901"
        )

        whenever(userRepository.findByUsername("existinguser")).thenReturn(
            UserApp(
                username = "existinguser",
                password = "encodedPassword",
                firstName = "Existing",
                lastName = "User",
                phone = "1234567890"
            )
        )

        val exception = assertThrows<UsernameAlreadyExistsException> {
            userAppService.register(request)
        }

        assertEquals("User already exists with the username existinguser", exception.message)
    }
}