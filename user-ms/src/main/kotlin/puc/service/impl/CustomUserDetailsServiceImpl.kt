package puc.service.impl

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import puc.repository.UserAppRepository

@Service
class CustomUserDetailsServiceImpl(private val userRepository: UserAppRepository) : UserDetailsService {

    private val MESSAGE_ERRO_USER_NOT_FOUND = "User not found"

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(MESSAGE_ERRO_USER_NOT_FOUND)
        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            user.roles.map { SimpleGrantedAuthority(it) }
        )
    }
}
