package puc.user

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import puc.util.JwtUtil

@RestController
@RequestMapping("/login")
class UserController(val userService: UserService, val jwtUtil: JwtUtil) {

    @PostMapping
    fun login(@RequestBody request: LoginRequest): ResponseEntity<String> {
        val user = userService.login(request.username, request.password)
        return if (user != null) {
            val token = jwtUtil.generateToken(user)
            ResponseEntity.ok(token)
        } else {
            ResponseEntity.status(401).body("Unauthorized")
        }
    }
}

data class LoginRequest(val username: String, val password: String)