package puc.user

import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import puc.util.CipherUtil
import puc.util.JwtUtil

@Service
class UserAppService(val userRepository: UserAppRepository, val jwtUtil: JwtUtil, val cipherUtil: CipherUtil) {

    @Throws(HttpClientErrorException::class)
    fun login(username: String, password: String): String? {
        val user = userRepository.findByUsername(username)

        if (user != null && cipherUtil.decryptPassword(user.password) == password) {
            return jwtUtil.generateToken(SessionToken(username))
        }
        throw Exception("No permission!")
    }

    fun register(username: String, password: String): SessionToken {
        val encryptedPassword = cipherUtil.encryptPassword(password)

        userRepository.save(UserApp(username = username, password = encryptedPassword))
        return SessionToken(username)
    }
}