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
import puc.exception.custom.*
import puc.model.dto.request.ChangePasswordRequest
import puc.model.dto.request.RegisterRequest
import puc.model.dto.request.UpdateUserRequest
import puc.model.dto.response.UserResponse
import puc.model.enum.RoleEnum
import puc.model.sql.UserApp
import puc.repository.UserAppRepository
import puc.service.impl.UserAppServiceImpl
import puc.util.JwtUtil
import puc.util.UserMapperUtil
import java.time.LocalDateTime
import java.util.*

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
    private lateinit var updateUserRequest: UpdateUserRequest
    private lateinit var changePasswordRequest: ChangePasswordRequest

    @BeforeEach
    fun setUp() {
        request = RegisterRequest("username",
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

        updateUserRequest = UpdateUserRequest(emptySet(),
                "12345678912",
                "User",
                "Test",
                "test@email.com",
                "85999999999")

        changePasswordRequest = ChangePasswordRequest(
                "123456",
                "1234567"
        )
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

    @Test
    fun `should throw exception when receiving invalid token`() {
        Mockito.`when`(jwtUtil.validateRefreshToken(any())).thenReturn(false)

        assertThrows<InvalidCredentialsException> {
            userAppServiceImpl.refreshToken("INVALID_TOKEN_TEST")
        }
    }

    @Test
    fun `should throw exception when not finding user to get refreshed token`() {
        Mockito.`when`(jwtUtil.validateRefreshToken(any())).thenReturn(true)
        Mockito.`when`(jwtUtil.getUsernameFromToken(any())).thenReturn("username")
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(null)

        assertThrows<UserNotFoundException> {
            userAppServiceImpl.refreshToken("REFRESH_TOKEN_TEST_INVALID_USER")
        }
    }

    @Test
    fun `should return refreshed token`() {
        Mockito.`when`(jwtUtil.validateRefreshToken(any())).thenReturn(true)
        Mockito.`when`(jwtUtil.getUsernameFromToken(any())).thenReturn("username")
        Mockito.`when`(jwtUtil.generateToken(any(), any())).thenReturn("NEW_TOKEN_TEST")
        Mockito.`when`(jwtUtil.getExpirationTime(any())).thenReturn(1)
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(user)

        val refreshTokenResponse = userAppServiceImpl.refreshToken("REFRESH_TOKEN_TEST")

        assertEquals(refreshTokenResponse.token, "NEW_TOKEN_TEST")
        assertEquals(refreshTokenResponse.refreshToken, "REFRESH_TOKEN_TEST")
        assertEquals(refreshTokenResponse.tokenType, "Bearer")
        assertEquals(refreshTokenResponse.tokenExpiresIn, 1)
    }

    @Test
    fun `should throw exception when not finding user to update`() {
        Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.empty())

        assertThrows<UserNotFoundException> {
            userAppServiceImpl.updateUser(1,
                    updateUserRequest,
                    "username",
                    emptySet())
        }
    }

    @Test
    fun `should throw exception when a non-admin tries to update another user`() {
        Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(user))

        assertThrows<InvalidRoleException> {
            userAppServiceImpl.updateUser(1,
                    updateUserRequest,
                    "usernameNon-admin",
                    emptySet())
        }
    }

    @Test
    fun `should throw exception when trying to update itself without role`() {
        Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(user))

        assertThrows<InvalidRoleException> {
            userAppServiceImpl.updateUser(1,
                    updateUserRequest,
                    "username",
                    emptySet())
        }
    }

    @Test
    fun `should throw exception when trying to update user with already existing email`() {
        val userSameEmail = UserApp(2,
                "usernameSameEmail",
                "\$2a\$10\$ApGjP21HNxzEzHpZY3iwg.eZSsgWtPFuL5NR32z/1W9pMcVVSz5Ym",
                mutableSetOf(RoleEnum.ROLE_USER.toString()),
                "",
                "User",
                "Test",
                "test@email.com",
                "859999999",
                LocalDateTime.now(),
                null)

        val updateUserRequestWithRole = UpdateUserRequest(setOf(RoleEnum.ROLE_USER.toString()),
                "12345678912",
                "User",
                "Test",
                "test@email.com",
                "85999999999")

        Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(user))
        Mockito.`when`(userRepository.findByEmail(any())).thenReturn(userSameEmail)

        assertThrows<UserEmailAlreadyExistsException> {
            userAppServiceImpl.updateUser(1,
                    updateUserRequestWithRole,
                    "username",
                    emptySet())
        }
    }

    @Test
    fun `should throw exception when trying to update user with already existing document`() {
        val userSameDocument = UserApp(2,
                "usernameSameDocumento",
                "\$2a\$10\$ApGjP21HNxzEzHpZY3iwg.eZSsgWtPFuL5NR32z/1W9pMcVVSz5Ym",
                mutableSetOf(RoleEnum.ROLE_USER.toString()),
                "",
                "User",
                "Test",
                "userSameDocument@email.com",
                "859999999",
                LocalDateTime.now(),
                null)

        val updateUserRequestWithRole = UpdateUserRequest(setOf(RoleEnum.ROLE_USER.toString()),
                "12345678912",
                "User",
                "Test",
                "test@email.com",
                "85999999999")

        Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(user))
        Mockito.`when`(userRepository.findByEmail(any())).thenReturn(null)
        Mockito.`when`(userRepository.findByDocument(any())).thenReturn(userSameDocument)

        assertThrows<UserDocumentAlreadyExistsException> {
            userAppServiceImpl.updateUser(1,
                    updateUserRequestWithRole,
                    "username",
                    emptySet())
        }
    }

    @Test
    fun `should throw exception when trying to update user with invalid rule`() {
        val updateUserRequestWithInvalidRole = UpdateUserRequest(setOf("INVALID_ROLE_TEST"),
                "12345678912",
                "User",
                "Test",
                "test@email.com",
                "85999999999")

        Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(user))

        assertThrows<InvalidRoleException> {
            userAppServiceImpl.updateUser(1,
                    updateUserRequestWithInvalidRole,
                    "admin",
                    setOf(RoleEnum.ROLE_ADMIN.toString()))
        }
    }

    @Test
    fun `should update user itself successfully`() {
        val updateUserRequestWithRole = UpdateUserRequest(setOf(RoleEnum.ROLE_USER.toString()),
                "12345678912 updated",
                "User updated",
                "Test updated",
                "updated@email.com",
                "85999999999 updated")

        Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(user))

        val userResponse = userAppServiceImpl.updateUser(1,
                updateUserRequestWithRole,
                "username",
                setOf(RoleEnum.ROLE_USER.toString()))

        assertEquals(userResponse.phone, updateUserRequestWithRole.phone)
        assertEquals(userResponse.email, updateUserRequestWithRole.email)
        assertEquals(userResponse.roles, updateUserRequestWithRole.roles)
        assertEquals(userResponse.document, updateUserRequestWithRole.document)
        assertEquals(userResponse.firstName, updateUserRequestWithRole.firstName)
        assertEquals(userResponse.lastName, updateUserRequestWithRole.lastName)
    }

    @Test
    fun `should update another user successfully`() {
        val updateAnotherUserRequestWithRole = UpdateUserRequest(setOf(RoleEnum.ROLE_USER.toString()),
                "12345678912 updated",
                "User updated",
                "Test updated",
                "updated@email.com",
                "85999999999 updated")

        Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(user))

        val userResponse = userAppServiceImpl.updateUser(1,
                updateAnotherUserRequestWithRole,
                "admin",
                setOf(RoleEnum.ROLE_ADMIN.toString()))

        assertEquals(userResponse.phone, updateAnotherUserRequestWithRole.phone)
        assertEquals(userResponse.email, updateAnotherUserRequestWithRole.email)
        assertEquals(userResponse.roles, updateAnotherUserRequestWithRole.roles)
        assertEquals(userResponse.document, updateAnotherUserRequestWithRole.document)
        assertEquals(userResponse.firstName, updateAnotherUserRequestWithRole.firstName)
        assertEquals(userResponse.lastName, updateAnotherUserRequestWithRole.lastName)
    }

    @Test
    fun `should throw exception when not finding user to change password`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(null)

        assertThrows<UserNotFoundException> {
            userAppServiceImpl.changePassword("username", changePasswordRequest)
        }
    }

    @Test
    fun `should throw exception change password with wrong old password`() {
        val changePasswordRequestWrongOldPassword = ChangePasswordRequest(
                "1234567",
                "123456"
        )

        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(user)

        assertThrows<InvalidCredentialsException> {
            userAppServiceImpl.changePassword("username", changePasswordRequestWrongOldPassword)
        }
    }

    @Test
    fun `should change password successfully`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(user)

        val userResponse = userAppServiceImpl.changePassword("username", changePasswordRequest)

        assertEquals(userResponse.phone, user.phone)
        assertEquals(userResponse.roles.elementAt(0), user.roles.elementAt(0))
        assertEquals(userResponse.email, user.email)
        assertEquals(userResponse.username, user.username)
        assertEquals(userResponse.document, user.document)
        assertEquals(userResponse.firstName, user.firstName)
        assertEquals(userResponse.lastName, user.lastName)
    }

}