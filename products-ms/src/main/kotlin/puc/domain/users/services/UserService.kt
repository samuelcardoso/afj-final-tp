package puc.domain.users.services

import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import puc.application.consumers.UserClient
import puc.domain.users.model.User
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(): IUserService {
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    val userClient: UserClient = mock<UserClient>().apply {
        val user = Optional.of(User(1, "JohnDoe", setOf("ROLE_USER")))
        whenever(this.findById(1)).thenReturn(user);
        whenever(this.getMe()).thenReturn(user);
    }

    override fun findById(id: Long): User? {
        logger.info("Retrieving user from user microservice");
        val user = userClient.findById(id).getOrNull();
        logger.info("User retrieved from user microservice: $user");
        return user;
    }

    override fun getMe(): User? {
        logger.info("Retrieving authenticated user from user microservice");
        val user = userClient.getMe().getOrNull();
        logger.info("Authenticated user retrieved from user microservice: $user");
        return user;
    }

}