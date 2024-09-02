package puc.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import puc.exception.custom.UsernameAlreadyExistsException
import puc.model.dto.request.RegisterRequest
import puc.model.dto.response.UserResponse
import puc.model.sql.UserApp
import puc.repository.UserAppRepository
import puc.service.impl.UserAppServiceImpl
import puc.util.JwtUtil
import puc.util.UserMapperUtil
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class UserAppServiceImplTest {

    @Mock
    private lateinit var jwtUtil: JwtUtil

    @Mock
    private lateinit var userRepository: UserAppRepository

    @InjectMocks
    private lateinit var userAppServiceImpl: UserAppServiceImpl

    private lateinit var request: RegisterRequest
    private lateinit var user: UserApp

    @BeforeEach
    fun setUp() {
        request = RegisterRequest("Username",
                "123456",
                "User",
                "Test",
                "test@email.com",
                "85999999999",
                "12345678912")

        user = UserApp(1,
                "username",
                "123456",
                mutableSetOf("ROLE_USER"),
                "",
                "User",
                "Test",
                "test@email.com",
                "859999999",
                LocalDateTime.now(),
                null)
    }

    @Test
    fun `must register new user`() {
        val user = UserMapperUtil.toUserApp(UserResponse(
                1,
                request.username,
                setOf("ROLE_USER"),
                request.document,
                request.email,
                request.firstName,
                request.lastName,
                request.phone)
        )

        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(null)
        Mockito.`when`(userRepository.save(any())).thenReturn(user)

        val userResponse = userAppServiceImpl.register(request)

        assert(userResponse.roles.elementAt(0).equals(user.roles.elementAt(0)))
        assert(userResponse.username.equals(request.username))
        assert(userResponse.email.equals(request.email))
        assert(userResponse.phone.equals(request.phone))
        assert(userResponse.document.equals(request.document))
        assert(userResponse.firstName.equals(request.firstName))
        assert(userResponse.lastName.equals(request.lastName))
    }

    @Test
    fun `should throw exception when not finding user`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(user)

        assertThrows<UsernameAlreadyExistsException> {
            userAppServiceImpl.register(request)
        }
    }

}