package puc.user

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/users")
class UserController(val userService: UserAppService) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<String> {
        try {
            val token = userService.login(request.username, request.password)
            return ResponseEntity.ok(token)
        } catch (ex: Exception) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody request: LoginRequest) {
        userService.register(request.username, request.password)
        ResponseEntity.ok()
    }
}

data class LoginRequest(val username: String, val password: String)