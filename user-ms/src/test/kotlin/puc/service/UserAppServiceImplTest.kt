package puc.service

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import puc.exception.custom.UsernameAlreadyExistsException
import puc.model.dto.request.RegisterRequest
import puc.model.dto.response.UserResponse
import puc.model.sql.UserApp
import puc.repository.UserAppRepository
import puc.service.impl.CustomUserDetailsServiceImpl
import puc.service.impl.UserAppServiceImpl
import puc.util.JwtUtil
import puc.util.UserMapperUtil
import java.time.LocalDateTime

class UserAppServiceImplTest {

    private val jwtUtil: JwtUtil = mockk()
    private val userRepository: UserAppRepository = mockk()
    private val userAppServiceImpl = UserAppServiceImpl(userRepository, jwtUtil)
    private val passwordEncoder = BCryptPasswordEncoder()
    private val request = RegisterRequest("Username",
            "123456",
            "User",
            "Test",
            "test@email.com",
            "85999999999",
            "12345678912")

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

        every { userRepository.save(any()) } returns user
        every { userRepository.findByUsername(any()) } returns null

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
        val user = UserApp(1,
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

        every { userRepository.findByUsername(any()) } returns user

        assertThrows<UsernameAlreadyExistsException> {
            userAppServiceImpl.register(request)
        }
    }

}