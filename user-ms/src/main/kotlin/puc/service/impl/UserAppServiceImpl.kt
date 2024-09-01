package puc.service.impl

import jakarta.servlet.http.HttpServletRequest
import java.util.Objects
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import puc.exception.custom.InvalidCredentialsException
import puc.exception.custom.UserNotFoundException
import puc.exception.custom.UsernameAlreadyExistsException
import puc.model.dto.request.RegisterRequest
import puc.model.dto.response.LoginResponse
import puc.model.dto.response.UserResponse
import puc.model.enum.Role
import puc.repository.UserAppRepository
import puc.service.UserAppService
import puc.util.JwtUtil
import puc.util.UserMapperUtil

@Service
class UserAppServiceImpl (
    val userRepository: UserAppRepository,
    val jwtUtil: JwtUtil,
    val httpServletRequest: HttpServletRequest
): UserAppService {

    private val BEARER = "Bearer "

    private val MESSAGE_ERRO_USER_NOT_FOUND = "User not found"
    private val MESSAGE_ERRO_INVALID_CREDENTIALS = "Invalid credentials"
    private val MESSAGE_ERRO_USER_ALREADY_EXISTS = "User already exists with the username %s"

//    private val passwordEncoder = BCryptPasswordEncoder()

    override fun register(request: RegisterRequest) : UserResponse {
        if (Objects.nonNull(findUserByUsername(request.username)))
            throw UsernameAlreadyExistsException(MESSAGE_ERRO_USER_ALREADY_EXISTS.format(request.username))
          val role = if ("/users/register/admin" == httpServletRequest.requestURI) Role.ROLE_ADMIN.name else Role.ROLE_USER.name
//        val encodedPassword = passwordEncoder.encode(request.password)
        val user = UserMapperUtil.toUserApp(UserResponse(request.username, setOf(role)))

//      user.password = encodedPassword
        userRepository.save(user)
        return UserMapperUtil.toUserDTO(user)
    }

    override fun login(username: String, password: String): LoginResponse {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException(MESSAGE_ERRO_USER_NOT_FOUND)

//        if (!passwordEncoder.matches(password, user.password))
//            throw InvalidCredentialsException(MESSAGE_ERRO_INVALID_CREDENTIALS)

        var token = jwtUtil.generateToken(user.username, user.roles)
        val expiresIn = jwtUtil.getExpirationTime(token)
        val userDTO = UserMapperUtil.toUserDTO(user)
        token = BEARER + token

        return LoginResponse(token, expiresIn, userDTO)
    }

    override fun getUserInfo(username: String): UserResponse {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException(MESSAGE_ERRO_USER_NOT_FOUND)
        return UserMapperUtil.toUserDTO(user)
    }

    private fun findUserByUsername(username: String) = userRepository.findByUsername(username)
}
