package puc.service.impl

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.userdetails.UsernameNotFoundException
import puc.model.sql.UserApp
import puc.repository.UserAppRepository
import java.time.LocalDateTime

class CustomUserDetailsServiceImplTest {

    private val DEFAULT_ROLE = "ROLE_USER"

    private val userRepository: UserAppRepository = mockk()

    val customUserDetailsServiceImpl = CustomUserDetailsServiceImpl(userRepository)


    @Test
    fun `must load user by name`() {
        val user = UserApp(1,
                "username",
                "123456",
                mutableSetOf(DEFAULT_ROLE),
                "",
                "User",
                "Test",
                "test@email.com",
                "859999999",
                LocalDateTime.now(),
                null)

        every { userRepository.findByUsername(any()) } returns user

        val userDatail = customUserDetailsServiceImpl.loadUserByUsername("username")

        assert(userDatail.username.equals(user.username))
        assert(userDatail.password.equals(user.password))
        assert(userDatail.authorities.isNotEmpty())
        assert(userDatail.authorities.elementAt(0).toString().equals(user.roles.elementAt(0).toString()))
    }

    @Test
    fun whenInvalidArray_thenThrowsException() {
        every { userRepository.findByUsername(any()) } returns null

        assertThrows<UsernameNotFoundException> {
            customUserDetailsServiceImpl.loadUserByUsername("username")
        }
    }

}
