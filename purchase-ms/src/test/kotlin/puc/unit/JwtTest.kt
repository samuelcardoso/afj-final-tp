package puc.unit

import java.util.Optional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import puc.dto.response.UserResponse
import puc.gateway.UserMsRestTemplate
import puc.vo.JWT

@ExtendWith(MockitoExtension::class)
class JwtTest {

    @Test
    fun shouldCreateJWTObjectSuccessfully() {
        val token = "valid_token"
        val mockUserMsRestTemplate = Mockito.mock(UserMsRestTemplate::class.java)
        val userResponse = UserResponse(id = 1L, username = "testUser", roles = setOf("ROLE_USER"))

        Mockito.`when`(mockUserMsRestTemplate.getMe(token))
            .thenReturn(Optional.of(userResponse))

        val jwt = JWT.create(token, mockUserMsRestTemplate)

        assertNotNull(jwt)
        assertEquals(1L, jwt.id)
        assertEquals("testUser", jwt.username)
        assertEquals(setOf("ROLE_USER"), jwt.roles)
        assertEquals(token, jwt.token)
    }

    @Test
    fun shouldThrowExceptionWhenUserResponseIsEmpty() {
        val token = "invalid_token"
        val mockUserMsRestTemplate = Mockito.mock(UserMsRestTemplate::class.java)

        Mockito.`when`(mockUserMsRestTemplate.getMe(token))
            .thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            JWT.create(token, mockUserMsRestTemplate)
        }

        assertEquals("Failed to authenticate.", exception.message)
    }

    @Test
    fun shouldThrowExceptionWhenUserIdIsNull() {
        val token = "valid_token"
        val mockUserMsRestTemplate = Mockito.mock(UserMsRestTemplate::class.java)
        val userResponse = UserResponse(id = null, username = "testUser", roles = setOf("ROLE_USER"))

        Mockito.`when`(mockUserMsRestTemplate.getMe(token))
            .thenReturn(Optional.of(userResponse))

        val exception = assertThrows<RuntimeException> {
            JWT.create(token, mockUserMsRestTemplate)
        }

        assertEquals("It wat not possible to create the object. ID not present in the response.", exception.message)
    }

}
