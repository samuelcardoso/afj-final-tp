package puc.service.impl

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.security.core.userdetails.UsernameNotFoundException
import puc.model.enum.RoleEnum
import puc.model.sql.UserApp
import puc.repository.UserAppRepository
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class CustomUserDetailsServiceImplTest {

    @Mock
    private lateinit var userRepository: UserAppRepository

    @InjectMocks
    private lateinit var customUserDetailsServiceImpl: CustomUserDetailsServiceImpl

    private lateinit var user: UserApp

    @BeforeEach
    fun setUp() {
        user = UserApp(1,
                "username",
                "123456",
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
    fun `must load user by name`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(user)

        val userDatail = customUserDetailsServiceImpl.loadUserByUsername("username")

        assert(userDatail.username.equals(user.username))
        assert(userDatail.password.equals(user.password))
        assert(userDatail.authorities.isNotEmpty())
        assert(userDatail.authorities.elementAt(0).toString().equals(user.roles.elementAt(0).toString()))
    }

    @Test
    fun `should throw exception when not finding user`() {
        Mockito.`when`(userRepository.findByUsername(any())).thenReturn(null)

        assertThrows<UsernameNotFoundException> {
            customUserDetailsServiceImpl.loadUserByUsername("username")
        }
    }

}
