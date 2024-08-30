package puc.application.consumers

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import puc.domain.users.model.User
import puc.infrastructure.config.UserClientConfig
import java.util.Optional

@FeignClient(name="\${client.user.name}", url = "\${client.user.url}", path = "\${client.user.path}", configuration = [UserClientConfig::class])
interface UserClient {

    @GetMapping("/{userId}")
    fun findById(@PathVariable("userId") userId: Long): Optional<User>;

    @GetMapping("/{userId}")
    fun getMe(): Optional<User>;

}