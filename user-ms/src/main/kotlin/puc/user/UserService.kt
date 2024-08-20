package puc.user

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {

    private val bCryptPasswordEncoder = BCryptPasswordEncoder()

    fun login(username: String, password: String): User? {
        val user = userRepository.findByUsername(username)
        return if (user != null && bCryptPasswordEncoder.matches(password, user.password)) {
            user
        } else {
            null
        }
    }

    fun register(username: String, password: String): User {
        val encryptedPassword = bCryptPasswordEncoder.encode(password)
        return userRepository.save(User(username = username, password = encryptedPassword))
    }
}