package puc.service.impl

import java.util.Objects
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import puc.exception.custom.UserDocumentAlreadyExistsException
import puc.exception.custom.InvalidRoleException
import puc.exception.custom.UserEmailAlreadyExistsException
import puc.exception.custom.InvalidCredentialsException
import puc.exception.custom.UserNotFoundException
import puc.exception.custom.UsernameAlreadyExistsException
import puc.model.dto.request.ChangePasswordRequest
import puc.model.dto.request.RegisterRequest
import puc.model.dto.request.UpdateUserRequest
import puc.model.dto.response.LoginResponse
import puc.model.dto.response.RefreshTokenResponse
import puc.model.dto.response.UserResponse
import puc.model.sql.UserApp
import puc.repository.UserAppRepository
import puc.service.UserAppService
import puc.util.JwtUtil
import puc.util.UserMapperUtil

@Service
class UserAppServiceImpl (
    val userRepository: UserAppRepository,
    val jwtUtil: JwtUtil
): UserAppService {
    private var DEFAULT_ROLE = "ROLE_USER"

    private val BEARER = "Bearer"

    private val MESSAGE_ERRO_USER_NOT_FOUND = "User not found"
    private val MESSAGE_ERRO_INVALID_CREDENTIALS = "Invalid credentials"
    private val MESSAGE_ERRO_INVALID_OLD_PASSWORD = "Invalid old password"
    private val MESSAGE_ERRO_INVALID_REFRESH_TOKEN = "Invalid refresh token"
    private val MESSAGE_ERRO_USER_ALREADY_EXISTS = "User already exists with the username %s"
    private val MESSAGE_ERRO_USER_EMAIL_ALREADY_EXISTS = "User already exists with the email %s"
    private val MESSAGE_ERRO_USER_DOCUMENT_ALREADY_EXISTS = "User already exists with the document %s"
    private val MESSAGE_ERRO_INVALID_ROLE = "Invalid role %s"

    private val passwordEncoder = BCryptPasswordEncoder()

    override fun register(request: RegisterRequest) : UserResponse {
        if (Objects.nonNull(findUserByUsername(request.username)))
            throw UsernameAlreadyExistsException(MESSAGE_ERRO_USER_ALREADY_EXISTS.format(request.username))

        val encodedPassword = passwordEncoder.encode(request.password)
        val user = UserMapperUtil.toUserApp(UserResponse(null, request.username, setOf(DEFAULT_ROLE)))
        user.password = encodedPassword
        userRepository.save(user)
        return UserMapperUtil.toUserDTO(user)
    }

    override fun login(username: String, password: String): LoginResponse {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException(MESSAGE_ERRO_USER_NOT_FOUND)

        if (!passwordEncoder.matches(password, user.password))
            throw InvalidCredentialsException(MESSAGE_ERRO_INVALID_CREDENTIALS)

        val token = jwtUtil.generateToken(user.username, user.roles)
        val refreshToken = jwtUtil.generateRefreshToken(user.username, user.roles)
        val expiresIn = jwtUtil.getExpirationTime(token)
        val userDTO = UserMapperUtil.toUserDTO(user)

        return LoginResponse(
            userDTO,
            token,
            BEARER,
            expiresIn,
            refreshToken
        )
    }

    override fun getUserInfo(username: String): UserResponse {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException(MESSAGE_ERRO_USER_NOT_FOUND)
        return UserMapperUtil.toUserDTO(user)
    }

    private fun findUserByUsername(username: String) = userRepository.findByUsername(username)

    override fun refreshToken(refreshToken: String): RefreshTokenResponse {
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw InvalidCredentialsException(MESSAGE_ERRO_INVALID_REFRESH_TOKEN)
        }

        val username = jwtUtil.getUsernameFromToken(refreshToken)
        val user = findUserByUsername(username) ?: throw UserNotFoundException(MESSAGE_ERRO_USER_NOT_FOUND)

        val newToken = jwtUtil.generateToken(user.username, user.roles)
        val expiresIn = jwtUtil.getExpirationTime(newToken)

        return RefreshTokenResponse(
            newToken,
            BEARER,
            expiresIn,
            refreshToken
        )
    }

    override fun updateUser(id: Long, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException(MESSAGE_ERRO_USER_NOT_FOUND) }
        this.checkUserUniqueness(user, request)
        this.checkIfRolesAreValid(request.roles)
        user.email = request.email
        user.document = request.document
        user.roles = request.roles.toMutableSet()
        user.firstName = request.firstName
        user.lastName = request.lastName
        user.phone = request.phone
        userRepository.save(user)
        return UserMapperUtil.toUserDTO(user)
    }

    private fun checkUserUniqueness(user: UserApp, request: UpdateUserRequest) {
        val userWithSameEmail = userRepository.findByEmail(request.email)
        if (userWithSameEmail != null && userWithSameEmail.id != user.id) {
            throw UserEmailAlreadyExistsException(MESSAGE_ERRO_USER_EMAIL_ALREADY_EXISTS.format(request.email))
        }
        val userWithSameDocument = userRepository.findByDocument(request.document)
        if (userWithSameDocument != null && userWithSameDocument.id != user.id) {
            throw UserDocumentAlreadyExistsException(MESSAGE_ERRO_USER_DOCUMENT_ALREADY_EXISTS.format(request.document))
        }
    }

    private fun checkIfRolesAreValid(roles: Set<String>) {
        val validRoles = setOf("ROLE_USER", "ROLE_USER_CLIENT")

        roles.forEach {
            if (!validRoles.contains(it)) {
                throw InvalidRoleException(MESSAGE_ERRO_INVALID_ROLE.format(it))
            }
        }
    }

    override fun changePassword(username: String, request: ChangePasswordRequest): UserResponse {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException(MESSAGE_ERRO_USER_NOT_FOUND)
        if (!passwordEncoder.matches(request.oldPassword, user.password)) {
            throw InvalidCredentialsException(MESSAGE_ERRO_INVALID_OLD_PASSWORD)
        }
        val encodedPassword = passwordEncoder.encode(request.newPassword)
        user.password = encodedPassword
        userRepository.save(user)
        return UserMapperUtil.toUserDTO(user)
    }
}
