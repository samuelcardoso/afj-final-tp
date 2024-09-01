package puc.user

import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import puc.util.JwtUtil

@Service
class UserAppService(val userRepository: UserAppRepository, val jwtUtil: JwtUtil) {

    @Throws(HttpClientErrorException::class)
    fun login(username: String, password: String): String? {
        val user = userRepository.findByUsername(username)

        if (user != null && user.password == password) {
            return jwtUtil.generateToken(SessionToken(username))
        }
        throw Exception("No permission!")
    }

    fun register(username: String, password: String): SessionToken {
        userRepository.save(UserApp(username = username, password = password))
        return SessionToken(username)
    }
}