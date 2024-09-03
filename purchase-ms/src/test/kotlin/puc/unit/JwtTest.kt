package puc.unit

import java.util.Optional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import puc.dto.response.UserResponse
import puc.gateway.UserMsRestTemplate
import puc.util.JwtUtil

@ExtendWith(MockitoExtension::class)
class JwtUtilTest {
    @Mock
    private val userMsRestTemplate: UserMsRestTemplate? = null

    @InjectMocks
    private val jwtUtil: JwtUtil? = null
    @Test
    fun testGetUserIdSuccess() {
        val token = "valid-token"
        val userResponse = UserResponse()
        Mockito.`when`(userMsRestTemplate!!.getMe(token)).thenReturn(Optional.of(userResponse))
        val userId = jwtUtil!!.getUserId(token)
        Assertions.assertEquals(0L, userId)
    }

    @Test
    fun testGetUserIdAuthenticationFailure() {
        val token = "invalid-token"
        Mockito.`when`(userMsRestTemplate!!.getMe(token)).thenReturn(Optional.empty())
        val exception = Assertions.assertThrows(
            RuntimeException::class.java
        ) { jwtUtil!!.getUserId(token) }
        Assertions.assertEquals("Failed to authenticate.", exception.message)
    }
}
