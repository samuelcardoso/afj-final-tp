package puc.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import puc.exception.custom.InvalidCredentialsException
import puc.exception.custom.UserNotFoundException
import puc.exception.custom.UsernameAlreadyExistsException
import puc.model.dto.request.RegisterRequest
import puc.model.dto.response.UserResponse
import puc.model.enum.RoleEnum
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
                "\$2a\$10\$ApGjP21HNxzEzHpZY3iwg.eZSsgWtPFuL5NR32z/1W9pMcVVSz5Ym",
                mutableSetOf(RoleEnum.ROLE_USER.toString()),
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
                setOf(RoleEnum.ROLE_USER.toString()),
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
    fun `should throw exception for already existing user`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(user)

        assertThrows<UsernameAlreadyExistsException> {
            userAppServiceImpl.register(request)
        }
    }

    @Test
    fun `should throw exception when not finding user to login`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(null)

        assertThrows<UserNotFoundException> {
            userAppServiceImpl.login("username", "123456")
        }
    }

    @Test
    fun `should throw exception for invalid password`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(user)

        assertThrows<InvalidCredentialsException> {
            userAppServiceImpl.login("username", "abc")
        }
    }

    @Test
    fun `must complete login successfully`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(user)
        Mockito.`when`(jwtUtil.generateToken(any(), any())).thenReturn("TOKEN_TEST")
        Mockito.`when`(jwtUtil.generateRefreshToken(any(), any())).thenReturn("REFRESH_TOKEN_TEST")
        Mockito.`when`(jwtUtil.getExpirationTime(any())).thenReturn(123)

        val loginResponse = userAppServiceImpl.login("username", "123456")

        assertEquals(loginResponse.user.id, user.id)
        assertEquals(loginResponse.user.roles.elementAt(0), user.roles.elementAt(0))
        assertEquals(loginResponse.user.username, user.username)
        assertEquals(loginResponse.user.email, user.email)
        assertEquals(loginResponse.user.phone, user.phone)
        assertEquals(loginResponse.user.document, user.document)
        assertEquals(loginResponse.user.firstName, user.firstName)
        assertEquals(loginResponse.user.lastName, user.lastName)
        assertEquals(loginResponse.token, "TOKEN_TEST")
        assertEquals(loginResponse.refreshToken, "REFRESH_TOKEN_TEST")
        assertEquals(loginResponse.tokenExpiresIn, 123)
        assertEquals(loginResponse.tokenType, "Bearer")
    }

    @Test
    fun `should throw exception when not finding user to get info`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(null)

        assertThrows<UserNotFoundException> {
            userAppServiceImpl.getUserInfo("username")
        }
    }

    @Test
    fun `should return user information`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(user)

        val userResponse = userAppServiceImpl.getUserInfo("username")

        assertEquals(userResponse.phone, user.phone)
        assertEquals(userResponse.roles.elementAt(0), user.roles.elementAt(0))
        assertEquals(userResponse.email, user.email)
        assertEquals(userResponse.username, user.username)
        assertEquals(userResponse.document, user.document)
        assertEquals(userResponse.firstName, user.firstName)
        assertEquals(userResponse.lastName, user.lastName)
    }

}